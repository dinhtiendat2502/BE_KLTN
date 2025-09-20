package com.app.toeic.service;

import com.app.toeic.model.Exam;
import com.app.toeic.model.Topic;
import com.app.toeic.response.ExamVO;
import com.app.toeic.response.ResponseVO;

import java.util.List;
import java.util.Optional;

public interface ExamService {
    ResponseVO getAllExam();

    ResponseVO addExam(Exam exam);

    ResponseVO updateExam(Exam exam);

    ResponseVO removeExam(Integer examId);

    Optional<Exam> findById(Integer examId);

    Optional<Exam> findExamWithPart(Integer examId);

    Object getAllExamByTopic(Integer topicId);

    Optional<ExamVO.ExamList> findExamByExamId(Integer examId);

    Optional<ExamVO.ExamFullQuestion> findExamWithFullQuestion(Integer examId);
}
