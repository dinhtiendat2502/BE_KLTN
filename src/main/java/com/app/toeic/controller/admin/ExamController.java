package com.app.toeic.controller.admin;


import com.app.toeic.dto.ExamDto;
import com.app.toeic.model.Exam;
import com.app.toeic.model.Question;
import com.app.toeic.repository.ITopicRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import com.app.toeic.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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


}
