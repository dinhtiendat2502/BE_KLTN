package com.app.toeic.transcript.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.repo.FirebaseRepository;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.app.toeic.transcript.service.RevAITranscriptService;
import com.app.toeic.translate.service.TranslateService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
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
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("transcript")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TranscriptController {
    RevAIConfigRepo revAIConfigRepo;
    FirebaseRepository firebaseRepository;
    TranscriptRepo transcriptRepo;
    FirebaseStorageService firebaseStorageService;
    RevAITranscriptService revAITranscriptService;

    @PostMapping(value = "get/google", consumes = {"multipart/form-data"})
    public Object getTranscript(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "name") String name
    ) throws IOException {
        // check file is mp3
        if (!Objects.requireNonNull(file.getContentType()).startsWith("audio/")) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("FILE_NOT_SUPPORT")
                    .build();
        }

        var firebaseConfig = firebaseRepository.findAllByStatus(true).stream().findFirst();
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
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
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

            var results = recognizeResponse.get().getResultsList();
            for (var result : results) {
                var alternative = result.getAlternativesList().getFirst();
                rs.append(alternative.getTranscript());
            }
            //            var translate = translateService.translate(rs.toString());

            return ResponseVO
                    .builder()
                    .data(rs.toString())
                    .success(true)
                    .message("TRANSCRIPT_SUCCESS")
                    .build();
        } catch (Exception e) {
            log.log(Level.WARNING, "TranscriptController >> getTranscript >> error: {}", e);
            return ResponseVO
                    .builder()
                    .data(e.getMessage())
                    .success(false)
                    .message("TRANSCRIPT_FAILED")
                    .build();
        }
    }


    @PostMapping(value = "get/revai", consumes = {"multipart/form-data"})
    public Object getTranscriptV2(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "name") String name
    ) throws IOException {
        if(file == null || file.isEmpty()) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("FILE_NOT_PRESENT")
                    .build();
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("audio/")) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message("FILE_NOT_SUPPORT")
                    .build();
        }
        var fileUrl = firebaseStorageService.uploadFile(file);

        var transcriptHistory = TranscriptHistory
                .builder()
                .transcriptName(name)
                .transcriptAudio(fileUrl)
                .build();
        var transcriptHistory1 = transcriptRepo.save(transcriptHistory);
        revAITranscriptService.getTranscript(fileUrl, transcriptHistory1);
        return ResponseVO.builder()
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
        return ResponseVO.builder()
                .data(transcript)
                .success(true)
                .message("GET_REV_AI_JOB_SUCCESS")
                .build();
    }


}
