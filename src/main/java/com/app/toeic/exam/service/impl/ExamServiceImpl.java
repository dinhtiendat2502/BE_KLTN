package com.app.toeic.exam.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.exam.response.ExamVO;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final IExamRepository examRepository;
    private final PartService partService;

    @Override
    public Object getAllExam() {
        return examRepository.findAllByStatus("ACTIVE");
    }

    @Override
    @Transactional
    public Object addExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), Integer.MIN_VALUE))) {
            throw new AppException(HttpStatus.SEE_OTHER, "EXAM_EXISTED");
        }
        var returnExam = examRepository.save(exam);
        partService.init7PartForExam(returnExam);
        return "CREATE_EXAM_SUCCESS";
    }

    @Override
    @Transactional
    public Object updateExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), exam.getExamId()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "EXAM_NAME_EXISTED");
        }
        examRepository.save(exam);
        return "UPDATE_EXAM_SUCCESS";
    }

    @Override
    public Object removeExam(Integer examId) {
        var exam = examRepository
                .findById(examId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "EXAM_NOT_FOUND"));
        exam.setStatus("INACTIVE");
        examRepository.save(exam);
        return "DELETE_EXAM_SUCCESS";
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
                .flatMap(part -> part
                        .getQuestions()
                        .stream())
                .filter(question -> Objects.equals(question.getQuestionId(), questionId))
                .map(ExamVO.ExamFullQuestionWithAnswer.Part.Question::getCorrectAnswer)
                .findFirst()
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy câu hỏi"));
    }

    @Override
    public Optional<ExamVO.ExamFullQuestion> findExamPractice(int i, List<String> listPart) {
        return examRepository.findExamPractice(i, listPart);
    }
}
