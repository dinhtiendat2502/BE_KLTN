package com.app.toeic.topic.service;

import com.app.toeic.topic.model.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Object getAllTopic(int page, int size);

    Optional<Topic> getTopicById(Integer id);

    void saveTopic(Topic topic);

    void removeTopic(Integer topicId);

    List<Topic> getAllTopics();
}
