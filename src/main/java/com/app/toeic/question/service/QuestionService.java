package com.app.toeic.question.service;

import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import com.app.toeic.external.response.ResponseVO;

import java.util.List;

public interface QuestionService {
    void saveAllQuestion(List<Question> list);

    ResponseVO getAllQuestionByPartId(Part part);
    List<Question> getAllQuestionByPart(Part part);
    void removeQuestionByPart(Part part);

    Question findById(Integer id);

    void saveQuestion(Question question);
}
