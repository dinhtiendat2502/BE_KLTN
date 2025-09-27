package com.app.toeic.topic.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/topic")
@RequiredArgsConstructor
@RestController
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/list")
    public ResponseVO getAllTopics() {
        return topicService.getAllTopic();
    }
}
