package com.app.toeic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.model.Exam;
import com.app.toeic.model.Question;
import com.app.toeic.repository.IExamRepository;
import com.app.toeic.repository.IQuestionRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final IExamRepository examRepository;
    private final IQuestionRepository questionRepository;

    @Override
    public ResponseVO getAllExam() {
        return null;
    }

    @Override
    @Transactional
    public ResponseVO addExam(Exam exam) {
        if(Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName()))){
            throw new AppException(HttpStatus.SEE_OTHER, "Đề thi đã tồn tại!");
        }
        examRepository.save(exam);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("Thêm đề thi thành công!")
                .build();
    }

    @Override
    public ResponseVO updateExam(Exam exam) {
        return null;
    }

    @Override
    public ResponseVO removeExam(Integer examId) {
        var exam = examRepository.findById(examId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        exam.setStatus("INACTIVE");
        examRepository.save(exam);
        return ResponseVO.builder().success(Boolean.TRUE).message(String.format("Xóa đề thi %s thành công!", exam.getExamName())).build();
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
