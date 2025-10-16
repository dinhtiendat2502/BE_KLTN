package com.app.toeic.topic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.topic.service.TopicService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TopicServiceImpl implements TopicService {
    ITopicRepository iTopicRepository;

    @Override
    public Object getAllTopic(int page, int size) {
        return iTopicRepository.findAllByStatus("ACTIVE", PageRequest.of(page, size));
    }

    @Override
    public Optional<Topic> getTopicById(Integer id) {
        return iTopicRepository.findById(id);
    }

    @Override
    public void saveTopic(Topic topic) {
        if (Boolean.TRUE.equals(iTopicRepository.existsByTopicNameAndTopicIdNot(topic.getTopicName(), topic.getTopicId()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "TOPIC_EXISTED");
        }
        iTopicRepository.save(topic);
    }

    @Override
    public void removeTopic(Integer topicId) {
        var topic = iTopicRepository
                .findById(topicId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "TOPIC_NOT_FOUND"));
        topic.setStatus("INACTIVE");
        iTopicRepository.save(topic);
    }

    @Override
    public List<Topic> getAllTopics() {
        return iTopicRepository.findAllByStatusOrderByExamsDesc("ACTIVE");
    }
}
