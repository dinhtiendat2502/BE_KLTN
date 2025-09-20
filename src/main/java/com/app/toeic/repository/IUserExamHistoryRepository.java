package com.app.toeic.repository;

import com.app.toeic.model.UserAccount;
import com.app.toeic.model.UserExamHistory;
import com.app.toeic.response.UserExamHistoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUserExamHistoryRepository extends JpaRepository<UserExamHistory, Integer> {
    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u.userExamHistoryId = ?1")
    Optional<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);

    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u = ?1")
    Optional<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByUser(UserAccount user);
}
