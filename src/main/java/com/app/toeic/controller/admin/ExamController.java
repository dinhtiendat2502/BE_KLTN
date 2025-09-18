package com.app.toeic.controller.admin;


import com.app.toeic.dto.ExamDto;
import com.app.toeic.model.Exam;
import com.app.toeic.repository.ITopicRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequestMapping("/admin/exam")
@RestController
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;
    private final ITopicRepository topicService;

    @GetMapping("/list")
    public ResponseVO getAllExam() {
        return examService.getAllExam();
    }

    @PostMapping("/create-exam")
    public ResponseVO createExam(@Valid @RequestBody ExamDto examDto) {
        var exam = Exam.builder()
                .examName(examDto.getExamName())
                .audioPart1(examDto.getAudioPart1())
                .audioPart2(examDto.getAudioPart2())
                .audioPart3(examDto.getAudioPart3())
                .audioPart4(examDto.getAudioPart4())
                .examImage(examDto.getExamImage())
                .status("ACTIVE")
                .topic(examDto.getTopicId() != null ? topicService.findById(examDto.getTopicId()).orElse(null) : null)
                .build();
        return examService.addExam(exam);
    }

    @PatchMapping("/update-exam")
    public ResponseVO updateExam(@Valid @RequestBody ExamDto examDto) {
        var exam = examService.findById(examDto.getExamId()).orElse(null);
        if (exam == null) {
            return ResponseVO.builder()
                    .success(Boolean.FALSE)
                    .message("Không tìm thấy đề thi")
                    .build();
        }
        exam.setExamName(examDto.getExamName());
        exam.setTopic(examDto.getTopicId() != null ? topicService.findById(examDto.getTopicId()).orElse(null) : null);
        return examService.updateExam(exam);
    }

    @GetMapping("/find-by-id")
    public ResponseVO findById(@RequestParam Integer examId) {
        var exam = examService.findExamWithPart(examId).orElse(null);
        if (exam == null) {
            return ResponseVO.builder()
                    .success(Boolean.FALSE)
                    .message("Không tìm thấy đề thi")
                    .build();
        }
        return ResponseVO.builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("Lấy đề thi thành công")
                .build();
    }
}
