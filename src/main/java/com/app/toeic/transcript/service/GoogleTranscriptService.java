package com.app.toeic.transcript.service;

import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationTimedPollAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.common.util.concurrent.MoreExecutors;
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
    @Async("crawlDataExecutor")
    public void getTranscript(String url, TranscriptHistory transcriptHistory, String jsonFile) throws IOException {
        var startTime = System.currentTimeMillis();
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
        try (var speech = SpeechClient.create(speechSettings.build())) {
            var config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .build();
            var audio = RecognitionAudio.newBuilder().setUri(url).build();

            var response = speech.longRunningRecognizeAsync(config, audio);
            var transcript = new StringBuilder();
            ApiFutures.addCallback(response, new ApiFutureCallback<>() {
                @Override
                public void onSuccess(LongRunningRecognizeResponse result) {
                    var results = result.getResultsList();
                    for (var speechResult : results) {
                        var alternative = speechResult.getAlternativesList().getFirst();
                        transcript.append(alternative.getTranscript());
                    }
                    transcriptHistory.setTranscriptContent(transcript.toString());
                    transcriptHistory.setStatus("TRANSCRIBED");
                    transcriptRepo.save(transcriptHistory);
                }
                @Override
                public void onFailure(Throwable t) {
                    log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> onFailure: ", t);
                    transcriptHistory.setStatus("FAILED");
                    transcriptRepo.save(transcriptHistory);
                }
            }, MoreExecutors.directExecutor());

        } catch (Exception e) {
            transcriptHistory.setStatus("FAILED");
            transcriptRepo.save(transcriptHistory);
            log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> Exception: ", e);
        } finally {
            log.log(
                    Level.INFO,
                    "GoogleTranscriptService >> getTranscript >> Took: {0}ms",
                    System.currentTimeMillis() - startTime
            );
        }
    }

    public String getT(String url, String jsonFile) throws IOException {
        StringBuilder rs = new StringBuilder();
        var startTime = System.currentTimeMillis();
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
        try (var speech = SpeechClient.create(speechSettings.build())) {
            var config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.MP3)
                            .setLanguageCode("en-US")
                            .setEnableAutomaticPunctuation(true)
                            .setSampleRateHertz(16000)
                            .build();
            var audio = RecognitionAudio.newBuilder().setUri(url).build();

            var response = speech.longRunningRecognizeAsync(config, audio);
            while (!response.isDone()) {
                System.out.println("Waiting for response...");
                Thread.sleep(10000);
            }

            var results = response.get().getResultsList();

            for (var result : results) {
                var alternative = result.getAlternativesList().getFirst();
                rs.append(alternative.getTranscript());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "GoogleTranscriptService >> getTranscript >> Exception: ", e);
        } finally {
            log.log(
                    Level.INFO,
                    "GoogleTranscriptService >> getTranscript >> Took: {0}ms",
                    System.currentTimeMillis() - startTime
            );
        }
        return rs.toString();
    }
}
