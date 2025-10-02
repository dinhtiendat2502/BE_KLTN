package com.app.toeic.topic.service;

import com.app.toeic.topic.model.Topic;
import com.app.toeic.external.response.ResponseVO;

import java.util.List;

public interface TopicService {
    Object getAllTopic(int page, int size);

    ResponseVO getTopicById(Integer id);

    ResponseVO addTopic(Topic topic);

    ResponseVO removeTopic(Integer topicId);

    List<Topic> getAllTopics();
}
