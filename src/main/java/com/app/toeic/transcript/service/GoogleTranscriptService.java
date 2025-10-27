package com.app.toeic.transcript.service;

import com.app.toeic.exception.AppException;
import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.longrunning.OperationTimedPollAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.*;

import java.text.MessageFormat;
import java.util.concurrent.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GoogleTranscriptService {
    TranscriptRepo transcriptRepo;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Async("crawlDataExecutor")
    public void getTranscript(String url, TranscriptHistory transcriptHistory, String jsonFile) {
        var startTime = System.currentTimeMillis();
        try {
            CompletableFuture.supplyAsync(() -> {
                try {
                    var timedRetryAlgorithm =
                            OperationTimedPollAlgorithm.create(
                                    RetrySettings.newBuilder()
                                            .setInitialRetryDelay(Duration.ofMillis(500L))
                                            .setRetryDelayMultiplier(1.5)
                                            .setMaxRetryDelay(Duration.ofMillis(5000L))
                                            .setInitialRpcTimeout(Duration.ZERO) // ignored
                                            .setRpcTimeoutMultiplier(1.0) // ignored
                                            .setMaxRpcTimeout(Duration.ZERO) // ignored
                                            .setTotalTimeout(Duration.ofHours(24L)) // set polling timeout to 24 hours
                                            .build());
                    var speechSettings = SpeechSettings.newBuilder();
                    var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonFile.getBytes()));
                    speechSettings.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
                    speechSettings.longRunningRecognizeOperationSettings().setPollingAlgorithm(timedRetryAlgorithm);
                    return SpeechClient.create(speechSettings.build());
                } catch (Exception e) {
                    log.log(Level.SEVERE, "RevAITranscriptService >> getTranscript >> Error: {}", e);
                    throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "REV_AI_JOB_SUBMISSION_FAILED");
                }
            }, executorService).thenAccept(speech -> {
                var config =
                        RecognitionConfig.newBuilder()
                                .setEncoding(RecognitionConfig.AudioEncoding.MP3)
                                .setLanguageCode("en-US")
                                .setSampleRateHertz(16000)
                                .build();
                var audio = RecognitionAudio.newBuilder().setUri(url).build();
                var response = speech.longRunningRecognizeAsync(config, audio);
                try {
                    var isDone = response.isDone();
                    while (!isDone) {
                        log.info("GoogleTranscriptService >> getTranscript >> Waiting for response...");
                        isDone = response.isDone();
                        Thread.sleep(10000L);
                    }
                    getTranscript(transcriptHistory, response);
                } catch (Exception e) {
                    saveTranscriptHistory(transcriptHistory, Constant.FAILED);
                    log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> thenAccept >> Error: {}", e);
                    Thread.currentThread().interrupt();
                    throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE_JOB_SUBMISSION_FAILED");
                } finally {
                    log.log(
                            Level.INFO,
                            "GoogleTranscriptService >> getTranscript >> Took: {0}ms",
                            System.currentTimeMillis() - startTime
                    );
                }
            }).exceptionally(e -> {
                saveTranscriptHistory(transcriptHistory, Constant.FAILED);
                log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> exceptionally >> Error: {}", e);
                return null;
            });
        } catch (Exception e) {
            saveTranscriptHistory(transcriptHistory, Constant.FAILED);
            log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> Exception: ", e);
        }
    }

    private void getTranscript(
            TranscriptHistory transcriptHistory,
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response
    ) {
        try {
            var results = response.get().getResultsList();
            var transcript = new StringBuilder();
            for (var result : results) {
                var alternative = result.getAlternativesList().getFirst();
                transcript.append(alternative.getTranscript());
            }
            transcriptHistory.setTranscriptContent(transcript.toString());
            saveTranscriptHistory(transcriptHistory, "TRANSCRIBED");
        } catch (InterruptedException | ExecutionException e) {
            saveTranscriptHistory(transcriptHistory, Constant.FAILED);
            log.log(
                    Level.SEVERE,
                    "GoogleTranscriptService >> getTranscript >> thenAccept >> InterruptedException | ExecutionException >> Error: {}",
                    e
            );
            Thread.currentThread().interrupt();
            throw new AppException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "GOOGLE_JOB_SUBMISSION_FAILED"
            );
        }
    }

    private void saveTranscriptHistory(TranscriptHistory transcriptHistory, String status) {
        transcriptHistory.setStatus(status);
        transcriptRepo.save(transcriptHistory);
    }
}
