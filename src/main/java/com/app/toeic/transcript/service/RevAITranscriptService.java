package com.app.toeic.transcript.service;

import ai.rev.speechtotext.ApiClient;
import ai.rev.speechtotext.models.asynchronous.RevAiJobOptions;
import com.app.toeic.exception.AppException;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.app.toeic.util.HttpStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RevAITranscriptService {
    ApiClient apiClient;
    TranscriptRepo transcriptRepo;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Async("crawlDataExecutor")
    public void getTranscript(String url, TranscriptHistory transcriptHistory) {
        CompletableFuture.supplyAsync(() -> {
            try {
                var revAiOptions = buildRevAiJobOptions(url, transcriptHistory.getTranscriptName());
                var submittedJob = apiClient.submitJobUrl(revAiOptions);
                return submittedJob.getJobId();
            } catch (Exception e) {
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
                    log.log(Level.INFO, MessageFormat.format("RevAITranscriptService >> getTranscript >> Job is not complete yet {0}", jobDetails.getJobStatus()));
                    if ("transcribed".equalsIgnoreCase(jobDetails.getJobStatus().name())
                            || "failed".equalsIgnoreCase(jobDetails.getJobStatus().name())) {
                        isJobComplete = true;
                        transcriptHistory.setStatus(jobDetails.getJobStatus().name());
                        transcriptHistory.setTranscriptContent(apiClient.getTranscriptText(jobDetails.getJobId()));
                        transcriptRepo.save(transcriptHistory);
                    }
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> thenAccept >> Error: {}", e);
                transcriptHistory.setStatus("FAILED");
                transcriptRepo.save(transcriptHistory);
            }
        }).exceptionally(e -> {
            transcriptHistory.setStatus("FAILED");
            transcriptRepo.save(transcriptHistory);
            log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> exceptionally >> Error: {}", e);
            return null;
        });
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
