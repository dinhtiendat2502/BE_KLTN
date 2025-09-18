package com.app.toeic.service;

import com.app.toeic.model.Exam;
import com.app.toeic.response.ResponseVO;

public interface ExamService {
    ResponseVO getAllExam();
    ResponseVO addExam(Exam exam);
    ResponseVO updateExam(Exam exam);
    ResponseVO removeExam(Integer examId);
}
