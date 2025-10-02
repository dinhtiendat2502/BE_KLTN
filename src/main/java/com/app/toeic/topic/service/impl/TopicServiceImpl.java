package com.app.toeic.topic.service.impl;


import com.app.toeic.exception.AppException;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.topic.repo.ITopicRepository;
import com.app.toeic.external.response.ResponseVO;
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
    public ResponseVO getTopicById(Integer id) {
        return null;
    }

    @Override
    public ResponseVO addTopic(Topic topic) {
        if (Boolean.TRUE.equals(iTopicRepository.existsByTopicName(topic.getTopicName()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Bộ đề thi đã tồn tại!");
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iTopicRepository.save(topic))
                .message("Thêm bộ đề thi thành công!")
                .build();
    }

    @Override
    public ResponseVO removeTopic(Integer topicId) {
        var topic = iTopicRepository
                .findById(topicId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bộ đề thi"));
        topic.setStatus("INACTIVE");
        iTopicRepository.save(topic);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(String.format("Xóa bộ đề thi %s thành công!", topic.getTopicName()))
                .build();
    }

    @Override
    public List<Topic> getAllTopics() {
        return iTopicRepository.findAllByStatusOrderByExamsDesc("ACTIVE");
    }
}
