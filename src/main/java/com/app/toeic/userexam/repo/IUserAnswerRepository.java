package com.app.toeic.userexam.repo;

import com.app.toeic.userexam.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
}
