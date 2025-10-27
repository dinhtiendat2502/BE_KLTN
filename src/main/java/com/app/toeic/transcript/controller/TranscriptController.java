package com.app.toeic.transcript.controller;

import com.app.toeic.cache.FirebaseConfigCache;
import com.app.toeic.config.RevAiConfig;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.revai.model.RevAIConfig;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import com.app.toeic.transcript.model.TranscriptHistory;
import com.app.toeic.transcript.repo.TranscriptRepo;
import com.app.toeic.transcript.service.GoogleTranscriptService;
import com.app.toeic.transcript.service.RevAITranscriptService;
import com.app.toeic.translate.service.TranslateService;
import com.app.toeic.util.DatetimeUtils;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ai.rev.speechtotext.ApiClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("transcript")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TranscriptController {
    RevAIConfigRepo revAIConfigRepo;
    TranscriptRepo transcriptRepo;
    FirebaseStorageService firebaseStorageService;
    RevAITranscriptService revAITranscriptService;
    TranslateService translateService;
    GoogleTranscriptService googleTranscriptService;
    FirebaseConfigCache firebaseConfigCache;

    @PostMapping(value = "get/google", consumes = {"multipart/form-data"})
    public Object getTranscript(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "name") String name
    ) throws IOException {
        var validateFile = validateFile(file);
        if (StringUtils.isNotBlank(validateFile)) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message(validateFile)
                    .build();
        }
        var firebaseConfig = firebaseConfigCache.getConfigValue(true);
        if (StringUtils.isEmpty(firebaseConfig.getFileJson())) {
            return ResponseVO
                    .builder()
                    .data(null)
                    .success(true)
                    .message("FIREBASE_CONFIG_NOT_FOUND")
                    .build();
        }
        var fileUrl = firebaseStorageService.uploadFile(file, true);
        var transcriptHistory = TranscriptHistory
                .builder()
                .transcriptName(name)
                .modelType("GOOGLE")
                .transcriptAudio(fileUrl)
                .build();
        var result = transcriptRepo.save(transcriptHistory);
        googleTranscriptService.getTranscript(fileUrl, result, firebaseConfig.getFileJson());
        return ResponseVO.builder()
                .success(true)
                .message("GET_TRANSCRIPT_IN_PROGRESS")
                .build();
    }

    @PostMapping(value = "get/revai", consumes = {"multipart/form-data"})
    public Object getTranscriptV2(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "name") String name
    ) throws IOException {
        var validateFile = validateFile(file);
        if (StringUtils.isNotBlank(validateFile)) {
            return ResponseVO.builder()
                    .data(null)
                    .success(false)
                    .message(validateFile)
                    .build();
        }
        var token = getRevAiConfig()
                .orElseThrow(() -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "REV_AI_CONFIG_NOT_FOUND"));
        var apiClient = new ApiClient(token.getAccessToken());

        var fileUrl = firebaseStorageService.uploadFile(file);
        var transcriptHistory = TranscriptHistory
                .builder()
                .transcriptName(name)
                .modelType("REV_AI")
                .transcriptAudio(fileUrl)
                .build();
        var transcriptHistory1 = transcriptRepo.save(transcriptHistory);
        revAITranscriptService.getTranscript(fileUrl, transcriptHistory1, apiClient);
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

    @GetMapping("/history")
    public Object getTranscriptHistory(
            @RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
            @RequestParam(value = "dateTo", defaultValue = "") String dateTo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "all") String status,
            @RequestParam(value = "sort", defaultValue = "desc") String sort
    ) {
        log.log(
                Level.INFO,
                MessageFormat.format(
                        "TranscriptController >> getTranscriptHistory >> dateFrom: {0}, dateTo: {1}, page: {2}, size: {3}",
                        dateFrom,
                        dateTo,
                        page,
                        size
                )
        );
        var startDateTime = DatetimeUtils.getFromDate(dateFrom);
        var endDateTime = DatetimeUtils.getToDate(dateTo);
        var sortRequest = "asc".equalsIgnoreCase(sort)
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        var pageRequest = PageRequest.of(page, size, sortRequest);
        var result = "all".equalsIgnoreCase(status)
                ? transcriptRepo.findAllByCreatedAtBetween(startDateTime, endDateTime, pageRequest)
                : transcriptRepo.findAllByCreatedAtBetweenAndStatus(startDateTime, endDateTime, status, pageRequest);
        return ResponseVO.builder()
                .data(result)
                .success(true)
                .message("GET_TRANSCRIPT_HISTORY_SUCCESS")
                .build();
    }

    @GetMapping("translate/{id}")
    public Object getTranslate(@PathVariable("id") Long id) {
        var transcriptHistory = transcriptRepo.findById(id);
        final var msg = new String[1];
        final var success = new Boolean[1];
        success[0] = false;
        transcriptHistory.ifPresentOrElse(e -> {
            if (StringUtils.isBlank(e.getTranscriptContent())) {
                msg[0] = "TRANSCRIPT_NOT_FOUND";
            } else {
                var translate = translateService.translate(e.getTranscriptContent());
                e.setTranscriptContentTranslate(translate.toString());
                transcriptRepo.save(e);
                msg[0] = "TRANSLATE_SUCCESS";
                success[0] = true;
            }
        }, () -> msg[0] = "TRANSCRIPT_NOT_FOUND");
        return ResponseVO.builder()
                .success(success[0])
                .message(msg[0])
                .build();
    }

    private String validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "FILE_NOT_PRESENT";
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("audio/")) {
            return "FILE_NOT_SUPPORT";
        }
        return StringUtils.EMPTY;
    }

    private Optional<RevAIConfig> getRevAiConfig() {
        return revAIConfigRepo.findAllByStatus(true)
                .stream()
                .findFirst();
    }

}
