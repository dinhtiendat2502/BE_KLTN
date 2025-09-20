package com.app.toeic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.model.Exam;
import com.app.toeic.model.Question;
import com.app.toeic.model.Topic;
import com.app.toeic.repository.IExamRepository;
import com.app.toeic.repository.IQuestionRepository;
import com.app.toeic.response.ExamVO;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import com.app.toeic.service.PartService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final IExamRepository examRepository;
    private final PartService partService;

    @Override
    public ResponseVO getAllExam() {
        return ResponseVO.builder().success(Boolean.TRUE).data(examRepository.findAllByStatus("ACTIVE")).message("Lấy danh sách đề thi thành công").build();
    }

    @Override
    @Transactional
    public ResponseVO addExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), Integer.MIN_VALUE))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Đề thi đã tồn tại!");
        }
        var returnExam = examRepository.save(exam);
        partService.init7PartForExam(returnExam);
        return ResponseVO.builder().success(Boolean.TRUE).data(null).message("Thêm đề thi thành công!").build();
    }

    @Override
    @Transactional
    public ResponseVO updateExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), exam.getExamId()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Đề thi đã tồn tại!");
        }
        examRepository.save(exam);
        return ResponseVO.builder().success(Boolean.TRUE).data("").message("Cập nhật đề thi thành công!").build();
    }

    @Override
    public ResponseVO removeExam(Integer examId) {
        var exam = examRepository.findById(examId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        exam.setStatus("INACTIVE");
        examRepository.save(exam);
        return ResponseVO.builder().success(Boolean.TRUE).message(String.format("Xóa đề thi %s thành công!", exam.getExamName())).build();
    }

    @Override
    public Optional<Exam> findById(Integer examId) {
        return examRepository.findById(examId);
    }

    @Override
    public Optional<Exam> findExamWithPart(Integer examId) {
        return examRepository.findExamWithPart(examId);
    }

    @Override
    public Object getAllExamByTopic(Integer topicId) {
        if (topicId == null || topicId == 0) {
            return examRepository.findAllByStatus("ACTIVE");
        }
        if (topicId == -1) {
            return examRepository.findAllByTopicIsNull();
        }
        return examRepository.findAllByStatusAndTopicId(topicId);
    }

    @Override
    public Optional<ExamVO.ExamList> findExamByExamId(Integer examId) {
        return examRepository.findExamByExamId(examId);
    }

    @Override
    public Optional<ExamVO.ExamFullQuestion> findExamWithFullQuestion(Integer examId) {
        return examRepository.findExamWithFullQuestion(examId);
    }

    @Override
    public Optional<ExamVO.ExamFullQuestionWithAnswer> findExamFullQuestionWithAnswer(Integer examId) {
        return examRepository.findExamFullQuestionWithAnswer(examId);
    }

    @Override
    public String findCorrectAnswer(ExamVO.ExamFullQuestionWithAnswer examFullQuestionWithAnswer, Integer questionId) {
        return examFullQuestionWithAnswer
                .getParts()
                .stream()
                .flatMap(part -> part.getQuestions().stream())
                .filter(question -> Objects.equals(question.getQuestionId(), questionId))
                .map(ExamVO.ExamFullQuestionWithAnswer.Part.Question::getCorrectAnswer)
                .findFirst()
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy câu hỏi"));
    }

    @Override
    public Optional<ExamVO.ExamFullQuestion> findExamPractice(int i, List<String> listPart) {
        return examRepository.findExamPractice(i, listPart);
    }

    private int getPart(int questionNumber) {
        if (questionNumber <= 6) return 1;
        if (questionNumber <= 31) return 2;
        if (questionNumber <= 70) return 3;
        if (questionNumber <= 100) return 4;
        if (questionNumber <= 130) return 5;
        if (questionNumber <= 146) return 6;
        return 7;
    }
}
