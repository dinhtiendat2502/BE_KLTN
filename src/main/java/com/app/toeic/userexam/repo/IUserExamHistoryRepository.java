package com.app.toeic.userexam.repo;

import com.app.toeic.user.model.UserAccount;
import com.app.toeic.userexam.model.UserExamHistory;
import com.app.toeic.userexam.response.MyExamVO;
import com.app.toeic.userexam.response.UserExamHistoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserExamHistoryRepository extends JpaRepository<UserExamHistory, Integer> {
    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u.userExamHistoryId = ?1")
    Optional<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);

    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u = ?1")
    Optional<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByUser(UserAccount user);


    @Query("""
            SELECT u
            FROM UserExamHistory u
                 JOIN FETCH u.exam e
            WHERE u.user = ?1
            ORDER BY u.examDate DESC
             """)
    List<MyExamVO.MyExamList> findAllByUser(UserAccount user);


    @Query("""
                    SELECT u
                    FROM UserExamHistory u
                         JOIN FETCH u.exam e
                         JOIN FETCH u.userAnswers ua
                         JOIN FETCH ua.question q
                    WHERE TRUE
                        AND u.user = ?1
                        AND u.userExamHistoryId = ?2
            """)
    Optional<UserExamHistoryVO.UserExamHistoryDetail> findByUserExamHistoryId(UserAccount profile, Integer userExamHistoryId);
}
