package com.app.toeic.exam.controller;


import com.app.toeic.exam.payload.ExamDto;
import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.util.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/exam")
@RestController
@RequiredArgsConstructor
public class ExamAdminController {
    private final ExamService examService;
    private final ITopicRepository topicService;

    @GetMapping("/list")
    public ResponseVO getAllExam() {
        return examService.getAllExam();
    }


    @PostMapping("/create-exam")
    public ResponseVO createExam(@Valid @RequestBody ExamDto examDto) {
        var exam = Exam
                .builder()
                .examName(examDto.getExamName())
                .audioPart1(examDto.getAudioPart1())
                .audioPart2(examDto.getAudioPart2())
                .audioPart3(examDto.getAudioPart3())
                .audioPart4(examDto.getAudioPart4())
                .examImage(examDto.getExamImage())
                .status("ACTIVE")
                .topic(examDto.getTopicId() != null ? topicService
                        .findById(examDto.getTopicId())
                        .orElse(null) : null)
                .build();
        return examService.addExam(exam);
    }

    @PatchMapping("/update-exam")
    public ResponseVO updateExam(@Valid @RequestBody ExamDto examDto) {
        var exam = examService
                .findById(examDto.getExamId())
                .orElse(null);
        if (exam == null) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("Không tìm thấy đề thi")
                    .build();
        }
        exam.setExamName(examDto.getExamName());
        exam.setTopic(examDto.getTopicId() != null ? topicService
                .findById(examDto.getTopicId())
                .orElse(null) : null);
        return examService.updateExam(exam);
    }

    @GetMapping("/find-by-id")
    public ResponseVO findById(@RequestParam Integer examId) {
        var exam = examService
                .findExamWithPart(examId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));

        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("Lấy đề thi thành công")
                .build();
    }
}
