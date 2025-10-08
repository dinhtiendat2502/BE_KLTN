package com.app.toeic.transcript.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.repo.FirebaseRepository;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ai.rev.speechtotext.ApiClient;
import ai.rev.speechtotext.models.asynchronous.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("transcript")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TranscriptController {
    RevAIConfigRepo revAIConfigRepo;

    FirebaseRepository firebaseRepository;

    @PostMapping(value = "get/v1", consumes = {"multipart/form-data"})
    public Object getTranscript(
            @RequestParam(value = "file") MultipartFile file
    ) throws IOException {
        // check file is mp3
        if (!Objects.requireNonNull(file.getContentType()).startsWith("audio/")) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("FILE_NOT_SUPPORT")
                    .build();
        }

        var firebaseConfig = firebaseRepository.findByStatus(true);
        if (firebaseConfig.isEmpty()) {
            return ResponseVO
                    .builder()
                    .data(null)
                    .success(true)
                    .message("FIREBASE_CONFIG_NOT_FOUND")
                    .build();
        }
        var rs = new StringBuilder();
        var jsonContent = firebaseConfig.get().getFileJson();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
        try (SpeechClient speechClient = SpeechClient.create(
                SpeechSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build()
        )) {
            RecognitionAudio recognitionAudio =
                    RecognitionAudio
                            .newBuilder()
                            .setContent(ByteString.copyFrom(file.getBytes()))
                            .build();
            var audioEncoding = Objects.requireNonNull(file.getContentType()).startsWith("audio/")
                    ? RecognitionConfig.AudioEncoding.MP3
                    : RecognitionConfig.AudioEncoding.LINEAR16;
            var config =
                    RecognitionConfig
                            .newBuilder()
                            .setEncoding(audioEncoding)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();
            var recognizeResponse = speechClient.longRunningRecognizeAsync(config, recognitionAudio);

            List<SpeechRecognitionResult> results = recognizeResponse.get().getResultsList();
            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().getFirst();
                rs.append(alternative.getTranscript());
            }
        } catch (
                Exception e) {
            return ResponseVO
                    .builder()
                    .data(e.getMessage())
                    .success(false)
                    .message("TRANSCRIPT_FAILED")
                    .build();
        }
        return ResponseVO
                .builder()
                .data(rs.toString())
                .success(true)
                .message("TRANSCRIPT_SUCCESS")
                .build();
    }


    @PostMapping(value = "get/v2", consumes = {"multipart/form-data"})
    public Object getTranscriptV2(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("audio/")) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("FILE_NOT_SUPPORT")
                    .build();
        }
        var revAiConfig = revAIConfigRepo.findAllByStatus(true);
        if (CollectionUtils.isEmpty(revAiConfig) || StringUtils.isBlank(revAiConfig.getFirst().getAccessToken())) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("REV_AI_CONFIG_NOT_FOUND")
                    .build();
        }

        var apiClient = new ApiClient(revAiConfig.getFirst().getAccessToken());
        var revAiJob = apiClient.submitJobLocalFile(file.getInputStream());
        var newlyRefreshedRevAiJob = apiClient.getJobDetails(revAiJob.getJobId());
        return ResponseVO.builder()
                .data(newlyRefreshedRevAiJob)
                .success(true)
                .message("TRANSCRIPT_SUCCESS")
                .build();
    }

    @GetMapping("get-revai-job")
    public Object getRevAIJob(@RequestParam(value = "jobId") String jobId) throws IOException {
        var revAiConfig = revAIConfigRepo.findAllByStatus(true);
        if (CollectionUtils.isEmpty(revAiConfig) || StringUtils.isBlank(revAiConfig.getFirst().getAccessToken())) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("REV_AI_CONFIG_NOT_FOUND")
                    .build();
        }

        var apiClient = new ApiClient(revAiConfig.getFirst().getAccessToken());
        var revAiJob = apiClient.getJobDetails(jobId);
        return ResponseVO.builder()
                .data(revAiJob)
                .success(true)
                .message("GET_REV_AI_JOB_SUCCESS")
                .build();
    }

    @GetMapping("get-transcript-revai")
    public Object getTrancriptRevai(@RequestParam(value = "jobId") String jobId) throws IOException {
        var revAiConfig = revAIConfigRepo.findAllByStatus(true);
        if (CollectionUtils.isEmpty(revAiConfig) || StringUtils.isBlank(revAiConfig.getFirst().getAccessToken())) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("REV_AI_CONFIG_NOT_FOUND")
                    .build();
        }

        var apiClient = new ApiClient(revAiConfig.getFirst().getAccessToken());
        var transcript = apiClient.getTranscriptText(jobId);
        transcript = transcript.replace("\n","").replaceAll("Speaker \\d+\\s+\\d+:\\d+:\\d+\\s+", "");
        return ResponseVO.builder()
                .data(transcript)
                .success(true)
                .message("GET_REV_AI_JOB_SUCCESS")
                .build();
    }



    private RevAiJobOptions buildRevAiJobOptions(String link) {
        RevAiJobOptions revAiJobOptions = new RevAiJobOptions();
        revAiJobOptions.setSourceConfig(link);
        revAiJobOptions.setMetadata("My first submission");
        revAiJobOptions.setSkipPunctuation(false);
        revAiJobOptions.setSkipDiarization(false);
        revAiJobOptions.setFilterProfanity(true);
        revAiJobOptions.setRemoveDisfluencies(true);
        revAiJobOptions.setSpeakerChannelsCount(null);
        revAiJobOptions.setDeleteAfterSeconds(259200); // 30 days in seconds
        revAiJobOptions.setLanguage("en");
        revAiJobOptions.setTranscriber("machine_v2");
        return revAiJobOptions;
    }
}
