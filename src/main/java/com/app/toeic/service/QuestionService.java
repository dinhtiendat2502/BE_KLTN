package com.app.toeic.service;

import com.app.toeic.model.Part;
import com.app.toeic.model.Question;
import com.app.toeic.response.ResponseVO;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    void saveAllQuestion(List<Question> list);

    ResponseVO getAllQuestionByPartId(Part part);
    List<Question> getAllQuestionByPart(Part part);
    void removeQuestionByPart(Part part);

    Question findById(Integer id);

    void saveQuestion(Question question);
}
