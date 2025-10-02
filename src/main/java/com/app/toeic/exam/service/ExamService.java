package com.app.toeic.exam.service;

import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.response.ExamVO;
import com.app.toeic.external.response.ResponseVO;

import java.util.List;
import java.util.Optional;

public interface ExamService {
    Object getAllExam();

    Object addExam(Exam exam);

    Object updateExam(Exam exam);

    Object removeExam(Integer examId);

    Optional<Exam> findById(Integer examId);

    Optional<Exam> findExamWithPart(Integer examId);

    Object getAllExamByTopic(Integer topicId);

    Optional<ExamVO.ExamList> findExamByExamId(Integer examId);

    Optional<ExamVO.ExamFullQuestion> findExamWithFullQuestion(Integer examId);

    Optional<ExamVO.ExamFullQuestionWithAnswer> findExamFullQuestionWithAnswer(Integer examId);

    String findCorrectAnswer(ExamVO.ExamFullQuestionWithAnswer examFullQuestionWithAnswer, Integer questionId);

    Optional<ExamVO.ExamFullQuestion> findExamPractice(int i, List<String> listPart);
}
