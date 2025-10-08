package com.app.toeic.transcript.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.repo.FirebaseRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    FirebaseRepository firebaseRepository;

    @PostMapping("v1/get")
    public Object getTranscript(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "link", required = false) String link
    ) throws IOException {
        // check file is mp3 or mp4
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("audio/") || file.getContentType().startsWith("video/")) {
                return ResponseVO.builder()
                        .data(null)
                        .success(false)
                        .message("FILE_NOT_SUPPORT")
                        .build();
            }
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
            RecognitionAudio recognitionAudio;
            if (StringUtils.isNotBlank(link)) {
                recognitionAudio = RecognitionAudio
                        .newBuilder()
                        .setUri(link)
                        .build();
            } else {
                recognitionAudio =
                        RecognitionAudio
                                .newBuilder()
                                .setContent(ByteString.copyFrom(file.getBytes()))
                                .build();
            }
            var audioEncoding = Objects.requireNonNull(file.getContentType()).startsWith("audio/")
                    ? RecognitionConfig.AudioEncoding.MP3
                    : RecognitionConfig.AudioEncoding.LINEAR16;
            RecognitionConfig config =
                    RecognitionConfig
                            .newBuilder()
                            .setEncoding(audioEncoding)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .setEnableWordConfidence(true)
                            .setModel(Objects.requireNonNull(file.getContentType()).startsWith("video/") ? "video" : "Long")
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


}
