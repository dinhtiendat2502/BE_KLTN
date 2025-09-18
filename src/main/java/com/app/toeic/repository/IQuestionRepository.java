package com.app.toeic.repository;

import com.app.toeic.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IQuestionRepository extends JpaRepository<Question, Integer> {
}
