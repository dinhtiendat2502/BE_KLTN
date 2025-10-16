package com.app.toeic.question.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import com.app.toeic.question.repo.IQuestionRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.question.service.QuestionService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class QuestionServiceImpl implements QuestionService {
    IQuestionRepository questionRepository;

    @Override
    @Transactional
    public void saveAllQuestion(List<Question> list) {
        questionRepository.saveAll(list);
    }

    @Override
    public ResponseVO getAllQuestionByPartId(Part part) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(questionRepository.findAllByPart(part))
                .message("Lấy danh sách câu hỏi thành công!")
                .build();
    }

    public List<Question> getAllQuestionByPart(Part part) {
        return questionRepository.findAllByPart(part);
    }

    @Override
    @Transactional
    public void removeQuestionByPart(Part part) {
        questionRepository.deleteAllByPart(part);
    }

    @Override
    public Question findById(Integer id) {
        return questionRepository
                .findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy câu hỏi!"));
    }

    @Override
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }
}








