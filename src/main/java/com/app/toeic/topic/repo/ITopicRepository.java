package com.app.toeic.topic.repo;

import com.app.toeic.topic.model.Topic;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.topic.response.TopicUserExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ITopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findAllByStatus(String status);

    List<Topic> findAllByStatusOrderByExamsDesc(String status);

    Boolean existsByTopicName(String topicName);

    @Query("""
                SELECT t.topicId AS topicId,
                       t.topicName AS topicName
                FROM UserExamHistory  ueh
                INNER JOIN UserAccount ua ON ueh.user = ua AND ua.status = 'ACTIVE'
                INNER JOIN Exam e ON e = ueh.exam AND e.status = 'ACTIVE'
                INNER JOIN Topic t ON t = e.topic AND t.status = 'ACTIVE'
                WHERE ua = ?1
                GROUP BY t.topicId, t.topicName
                ORDER BY t.topicId ASC
            """)
    List<TopicUserExam> findAllByExamUserHistory(UserAccount userAccount);

}
