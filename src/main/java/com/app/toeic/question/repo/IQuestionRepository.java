package com.app.toeic.question.repo;

import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IQuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByPart(Part part);

    void deleteAllByPart(Part part);
}
