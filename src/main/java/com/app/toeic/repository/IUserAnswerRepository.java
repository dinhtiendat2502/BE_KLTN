package com.app.toeic.repository;

import com.app.toeic.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
}
