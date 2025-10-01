package com.app.toeic.topic.controller;


import com.app.toeic.topic.payload.TopicDTO;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/topic")
public class TopicAdminController {
    private final TopicService topicService;

    @GetMapping("/list")
    public ResponseVO getAllTopic() {
        return topicService.getAllTopic();
    }

    @PostMapping("/create-topic")
    public ResponseVO createTopic(@RequestBody TopicDTO topic) {
        var newTopic = Topic
                .builder()
                .topicName(topic.getTopicName())
                .topicImage(topic.getTopicImage())
                .status("ACTIVE")
                .build();
        return topicService.addTopic(newTopic);
    }

    @DeleteMapping("/delete/{topicId}")
    public ResponseVO deleteTopic(@PathVariable("topicId") Integer topicId) {
        return topicService.removeTopic(topicId);
    }
}
