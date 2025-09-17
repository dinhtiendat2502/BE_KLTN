package com.app.toeic.service.impl;


import com.app.toeic.exception.AppException;
import com.app.toeic.model.Topic;
import com.app.toeic.repository.ITopicRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.TopicService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final ITopicRepository iTopicRepository;

    @Override
    public ResponseVO getAllTopic() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iTopicRepository.findAllByStatus("ACTIVE"))
                .message("Lấy danh sách bộ đề thi thành công")
                .build();
    }

    @Override
    public ResponseVO getTopicById(Long id) {
        return null;
    }

    @Override
    public ResponseVO addTopic(Topic topic) {
        return ResponseVO.builder()
                .success(Boolean.TRUE)
                .data(iTopicRepository.save(topic))
                .message("Thêm bộ đề thi thành công!")
                .build();
    }

    @Override
    public ResponseVO removeTopic(Integer topicId) {
        var topic = iTopicRepository
                .findById(topicId)
                .orElseThrow(() ->
                        new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy bộ đề thi"));
        topic.setStatus("INACTIVE");
        iTopicRepository.save(topic);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(String.format("Xóa bộ đề thi %s thành công!", topic.getTopicName()))
                .build();
    }
}
