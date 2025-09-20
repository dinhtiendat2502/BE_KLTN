package com.app.toeic.repository;

import com.app.toeic.model.UserExamHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserExamHistoryRepository extends JpaRepository<UserExamHistory, Integer> {
}
