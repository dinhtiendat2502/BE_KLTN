package com.app.toeic.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamRestController {
    private final ExamService examService;

    @GetMapping("/list")
    public ResponseVO getAllExams() {
        return examService.getAllExam();
    }

    @GetMapping("/list-by-topic/{topicId}")
    public ResponseVO getAllExamByTopic(@PathVariable String topicId) {
        return ResponseVO.builder().success(Boolean.TRUE).data(examService.getAllExamByTopic(Integer.parseInt(topicId))).message("Lấy danh sách đề thi theo chủ đề thành công").build();
    }

    @GetMapping("/find-by-id/{examId}")
    public ResponseVO findById(@PathVariable("examId") String examId) {
        var exam = examService.findExamByExamId(Integer.parseInt(examId))
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        return ResponseVO.builder().success(Boolean.TRUE).data(exam).message("Lấy đề thi thành công").build();
    }
}
