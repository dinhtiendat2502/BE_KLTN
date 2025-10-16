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
import java.util.concurrent.Executor;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RevAITranscriptService {
    RevAIConfigRepo revAIConfigRepo;
    TranscriptRepo transcriptRepo;
    Executor crawlDataExecutor;

    @Async("crawlDataExecutor")
    public void getTranscript(String url, TranscriptHistory transcriptHistory) throws IOException {
        var revAiConfig = revAIConfigRepo.findAllByStatus(true);
        if (CollectionUtils.isEmpty(revAiConfig) || StringUtils.isBlank(revAiConfig.getFirst().getAccessToken())) {
            throw new AppException(HttpStatus.NOT_FOUND, "REV_AI_CONFIG_NOT_FOUND");
        }

        crawlDataExecutor.execute(() -> {
            try {
                var apiClient = new ApiClient(revAiConfig.getFirst().getAccessToken());
                var revAiOptions = buildRevAiJobOptions(url, transcriptHistory.getTranscriptName());
                var submittedJob = apiClient.submitJobUrl(revAiOptions);
                boolean isJobComplete = false;
                while (!isJobComplete) {
                    var jobDetails = apiClient.getJobDetails(submittedJob.getJobId());
                    log.log(Level.INFO, MessageFormat.format("RevAITranscriptService >> getTranscript >> Job is not complete yet {0}", jobDetails.getJobStatus()));
                    if ("transcribed".equalsIgnoreCase(jobDetails.getJobStatus().name())
                            || "failed".equalsIgnoreCase(jobDetails.getJobStatus().name())) {
                        isJobComplete = true;
                        transcriptHistory.setStatus(jobDetails.getJobStatus().name());
                        transcriptHistory.setJobRevAIId(jobDetails.getJobId());
                        transcriptHistory.setTranscriptContent(apiClient.getTranscriptText(jobDetails.getJobId()));
                        transcriptRepo.save(transcriptHistory);
                    }
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> Error: {}", e);
            }
        });
    }

    private RevAiJobOptions buildRevAiJobOptions(String link, String name) {
        var revAiJobOptions = new RevAiJobOptions();
        revAiJobOptions.setSourceConfig(link);
        revAiJobOptions.setMetadata(name);
        revAiJobOptions.setSkipPunctuation(false);
        revAiJobOptions.setSkipDiarization(false);
        revAiJobOptions.setFilterProfanity(true);
        revAiJobOptions.setRemoveDisfluencies(true);
        revAiJobOptions.setSpeakerChannelsCount(null);
        revAiJobOptions.setDeleteAfterSeconds(2592000);
        revAiJobOptions.setLanguage("en");
        revAiJobOptions.setTranscriber("machine_v2");
        return revAiJobOptions;
    }
}
