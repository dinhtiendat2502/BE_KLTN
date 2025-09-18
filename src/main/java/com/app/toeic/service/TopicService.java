package com.app.toeic.service;

import com.app.toeic.model.Topic;
import com.app.toeic.response.ResponseVO;

public interface TopicService {
    ResponseVO getAllTopic();
    ResponseVO getTopicById(Integer id);
    ResponseVO addTopic(Topic topic);
    ResponseVO removeTopic(Integer topicId);
}
