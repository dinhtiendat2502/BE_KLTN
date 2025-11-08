package com.app.toeic.topic.service.impl;


import com.app.toeic.exception.AppException;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.topic.service.TopicService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final ITopicRepository iTopicRepository;

    @Override
    public Object getAllTopic(int page, int size) {
        return iTopicRepository.findAllByStatus("ACTIVE", PageRequest.of(page, size));
    }

    @Override
    public Object getTopicById(Integer id) {
        return null;
    }

    @Override
    public Object addTopic(Topic topic) {
        if (Boolean.TRUE.equals(iTopicRepository.existsByTopicName(topic.getTopicName()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "TOPIC_EXISTED");
        }
        iTopicRepository.save(topic);
        return "CREATE_TOPIC_SUCCESS";
    }

    @Override
    public Object removeTopic(Integer topicId) {
        var topic = iTopicRepository
                .findById(topicId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "TOPIC_NOT_FOUND"));
        topic.setStatus("INACTIVE");
        iTopicRepository.save(topic);
        return "DELETE_TOPIC_SUCCESS";
    }

    @Override
    public List<Topic> getAllTopics() {
        return iTopicRepository.findAllByStatusOrderByExamsDesc("ACTIVE");
    }
}
