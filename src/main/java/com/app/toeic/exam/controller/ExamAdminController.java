package com.app.toeic.exam.controller;


import com.app.toeic.exam.payload.ExamDTO;
import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/exam")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ExamAdminController {
    ExamService examService;
    ITopicRepository topicService;
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


    @PostMapping("/create-exam")
    public Object createExam(@Valid @RequestBody ExamDTO examDto) {
        var exam = Exam
                .builder()
                .examName(examDto.examName())
                .audioPart1(examDto.audioPart1())
                .audioPart2(examDto.audioPart2())
                .audioPart3(examDto.audioPart3())
                .audioPart4(examDto.audioPart4())
                .examImage(examDto.examImage())
                .isFree(examDto.isFree())
                .numberOfUserDoExam(0)
                .price(0.0)
                .status(Constant.STATUS_ACTIVE)
                .topic(examDto.topicId() != null ? Topic.builder().topicId(examDto.topicId()).build() : null)
                .build();
        examService.addExam(exam);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CREATE_EXAM_SUCCESS")
                .build();
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
