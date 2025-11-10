package com.app.toeic.userexam.repo;

import com.app.toeic.user.model.UserAccount;
import com.app.toeic.userexam.model.UserExamHistory;
import com.app.toeic.userexam.response.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserExamHistoryRepository extends JpaRepository<UserExamHistory, Integer> {
    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u.userExamHistoryId = ?1")
    Optional<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);

    @Query("SELECT u FROM UserExamHistory u JOIN FETCH u.exam WHERE u.user = ?1")
    List<UserExamHistoryVO.UserExamHistoryGeneral> findUserExamHistoryByUser(UserAccount user);


    @Query("""
            SELECT u
            FROM UserExamHistory u
                 JOIN FETCH u.exam e
            WHERE u.user = ?1
            ORDER BY u.examDate DESC
             """)
    List<MyExamVO.MyExamList> findAllByUser(UserAccount user);

    @Query("""
                    SELECT ueh
                    FROM UserExamHistory ueh
                    JOIN FETCH ueh.user
                    JOIN FETCH ueh.exam e
                    WHERE e.examId = ?1
                    ORDER BY ueh.totalScore DESC
            """)
    List<ExamHistoryStatisticDTO> findAllByExam(Integer examId);

    @Query("""
                    SELECT ueh
                    FROM UserExamHistory ueh
                    JOIN FETCH ueh.user
                    JOIN FETCH ueh.exam e
                    WHERE e.isFree = false
                    ORDER BY ueh.totalScore DESC
            """)
    List<ExamHistoryStatisticV2DTO> findAllByRealExam();


    @Query("""
                    SELECT u
                    FROM UserExamHistory u
                         JOIN FETCH u.exam e
                         JOIN FETCH u.userAnswers ua
                         JOIN FETCH ua.question q
                         LEFT JOIN FETCH q.questionImages
                    WHERE TRUE
                        AND u.user = ?1
                        AND u.userExamHistoryId = ?2
                    order by q.questionNumber
            """)
    Optional<UserExamHistoryVO.UserExamHistoryDetail> findByUserExamHistoryId(
            UserAccount profile,
            Integer userExamHistoryId
    );

    @Query("""
                    SELECT u
                    FROM UserExamHistory u
                         JOIN FETCH u.exam e
                         JOIN FETCH u.userAnswers ua
                         JOIN FETCH ua.question q
                         LEFT JOIN FETCH q.questionImages
                    WHERE TRUE
                        AND u.userExamHistoryId = ?1
                    order by q.questionNumber
            """)
    Optional<UserExamHistoryVO.UserExamHistoryDetail> findByUserExamHistoryId(Integer userExamHistoryId);


    @Query("""
               SELECT u
               FROM UserExamHistory u
               JOIN FETCH u.exam e
               JOIN  FETCH u.user us
               ORDER BY u.examDate DESC
            """)
    List<UserExamHistoryInfo> findAllExamHistory();
}
