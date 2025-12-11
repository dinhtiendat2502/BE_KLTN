package com.app.toeic.transcript.service;

import ai.rev.speechtotext.ApiClient;
import ai.rev.speechtotext.models.asynchronous.RevAiJobOptions;
import ai.rev.speechtotext.models.asynchronous.RevAiJobStatus;
import com.app.toeic.exception.AppException;
import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RevAITranscriptService {
    TranscriptRepo transcriptRepo;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Async(Constant.TOEICUTE)
    public void getTranscript(String url, TranscriptHistory transcriptHistory, ApiClient apiClient) {
        CompletableFuture.supplyAsync(() -> {
            try {
                var revAiOptions = buildRevAiJobOptions(url, transcriptHistory.getTranscriptName());
                var submittedJob = apiClient.submitJobUrl(revAiOptions);
                return submittedJob.getJobId();
            } catch (Exception e) {
                saveTranscriptHistory(transcriptHistory, Constant.FAILED);
                log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> Error: {}", e);
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "REV_AI_JOB_SUBMISSION_FAILED");
            }
        }, executorService).thenAccept(jobId -> {
            boolean isJobComplete = false;
            try {
                var jobDetails = apiClient.getJobDetails(jobId);
                transcriptHistory.setJobRevAIId(jobDetails.getJobId());
                while (!isJobComplete) {
                    jobDetails = apiClient.getJobDetails(jobId);
                    log.log(
                            Level.INFO,
                            MessageFormat.format(
                                    "RevAITranscriptService >> getTranscript >> Job is not complete yet {0}",
                                    jobDetails.getJobStatus()
                            )
                    );
                    isJobComplete = RevAiJobStatus.TRANSCRIBED.equals(jobDetails.getJobStatus())
                            || RevAiJobStatus.FAILED.equals(jobDetails.getJobStatus());
                    Thread.sleep(3000L);
                }
                transcriptHistory.setTranscriptContent(apiClient.getTranscriptText(jobDetails.getJobId()));
                saveTranscriptHistory(transcriptHistory, jobDetails.getJobStatus().name());
            } catch (Exception e) {
                log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> thenAccept >> Error: {}", e);
                saveTranscriptHistory(transcriptHistory, Constant.FAILED);
                Thread.currentThread().interrupt();
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR");
            }
        }).exceptionally(e -> {
            saveTranscriptHistory(transcriptHistory, Constant.FAILED);
            log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> exceptionally >> Error: {}", e);
            return null;
        });
    }

    private void saveTranscriptHistory(TranscriptHistory transcriptHistory, String status) {
        transcriptHistory.setStatus(status);
        transcriptRepo.save(transcriptHistory);
    }

    private RevAiJobOptions buildRevAiJobOptions(String link, String name) {
        var revAiJobOptions = new RevAiJobOptions();
        revAiJobOptions.setSourceConfig(link);
        revAiJobOptions.setMetadata(name);
        revAiJobOptions.setSkipPunctuation(false);
        revAiJobOptions.setSkipDiarization(true);
        revAiJobOptions.setFilterProfanity(true);
        revAiJobOptions.setRemoveDisfluencies(false);
        revAiJobOptions.setSpeakerChannelsCount(null);
        revAiJobOptions.setDeleteAfterSeconds(2592000);
        revAiJobOptions.setLanguage("en");
        revAiJobOptions.setTranscriber("machine_v2");
        return revAiJobOptions;
    }
}
