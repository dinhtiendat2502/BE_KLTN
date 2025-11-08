package com.app.toeic.exam.controller;


import com.app.toeic.exam.payload.ExamDTO;
import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

@Log
@RequestMapping("/admin/exam")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ExamAdminController {
    ExamService examService;
    ITopicRepository topicService;
    FirebaseStorageService firebaseStorageService;
    static final String EXAM_NOT_FOUND = "EXAM_NOT_FOUND";

    @GetMapping("/list")
    public Object getAllExam() {
        var list = examService.getAllExam();
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(list)
                .message("GET_ALL_EXAM_SUCCESS")
                .build();
    }


    @PostMapping(value = "/create-exam", consumes = {"multipart/form-data"})
    @SneakyThrows
    public Object createExam(
            @RequestParam("examName") String examName,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "audio1", required = false) MultipartFile audio1,
            @RequestParam(value = "audio2", required = false) MultipartFile audio2,
            @RequestParam(value = "audio3", required = false) MultipartFile audio3,
            @RequestParam(value = "audio4", required = false) MultipartFile audio4,
            @RequestParam("isFree") Boolean isFree,
            @RequestParam(value = "topicId", required = false) Integer topicId,
            @RequestParam(value = "fromDate", required = false) LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false) LocalDateTime toDate
    ) {
        var exam = Exam
                .builder()
                .examName(examName)
                .isFree(isFree)
                .topic(topicId != -1 ? Topic.builder().topicId(topicId).build() : null)
                .build();
        uploadAudio(audio, exam, 0);
        uploadAudio(audio1, exam, 1);
        uploadAudio(audio2, exam, 2);
        uploadAudio(audio3, exam, 3);
        uploadAudio(audio4, exam, 4);
        if (!isFree) {
            exam.setFromDate(fromDate);
            exam.setToDate(toDate);
        }
        examService.addExam(exam);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CREATE_EXAM_SUCCESS")
                .build();
    }

    @SneakyThrows
    private void uploadAudio(MultipartFile audio, Exam exam, int index) {
        if (audio == null || audio.isEmpty()) return;
        var url = firebaseStorageService.uploadFile(audio);
        switch (index) {
            case 1 -> exam.setAudioPart1(url);
            case 2 -> exam.setAudioPart2(url);
            case 3 -> exam.setAudioPart3(url);
            case 4 -> exam.setAudioPart4(url);
            default -> exam.setExamAudio(url);
        }
    }

    @PatchMapping("/update-exam")
    public ResponseVO updateExam(@Valid @RequestBody ExamDTO examDto) {
        var exam = examService
                .findById(examDto.examId())
                .orElse(null);
        if (exam == null) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message(EXAM_NOT_FOUND)
                    .build();
        }
        exam.setExamName(StringUtils.defaultIfBlank(examDto.examName(), exam.getExamName()));
        exam.setFree(BooleanUtils.toBooleanDefaultIfNull(examDto.isFree(), exam.isFree()));
        exam.setTopic(examDto.topicId() != null ? topicService
                .findById(examDto.topicId())
                .orElse(null) : null);
        examService.save(exam);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_EXAM_SUCCESS")
                .build();
    }

    @DeleteMapping("/delete-exam/{examId}")
    public ResponseVO deleteExam(@PathVariable Integer examId) {
        var exam = examService
                .findById(examId);
        if (exam.isPresent()) {
            exam.get().setStatus(Constant.STATUS_INACTIVE);
            examService.save(exam.get());
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_EXAM_SUCCESS")
                .build();
    }

    @GetMapping("/find-by-id")
    public ResponseVO findById(@RequestParam Integer examId) {
        var exam = examService
                .findExamByExamId(examId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, EXAM_NOT_FOUND));

        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("GET_EXAM_SUCCESS")
                .build();
    }
}
