package com.app.toeic.controller;


import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RequestMapping("/topic")
@RequiredArgsConstructor
@RestController
public class TopicRestController {
    private final TopicService topicService;

    @GetMapping("/list")
    public ResponseVO getAllTopics() {
        return topicService.getAllTopic();
    }
}
