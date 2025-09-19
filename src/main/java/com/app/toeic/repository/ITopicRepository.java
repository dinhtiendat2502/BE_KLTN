package com.app.toeic.repository;

import com.app.toeic.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ITopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findAllByStatus(String status);

    List<Topic> findAllByStatusOrderByExamsDesc(String status);

    Boolean existsByTopicName(String topicName);
}
