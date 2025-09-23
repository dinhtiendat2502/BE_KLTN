package com.app.toeic.controller.admin;


import com.app.toeic.dto.TopicDto;
import com.app.toeic.model.Topic;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/topic")
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/list")
    public ResponseVO getAllTopic() {
        return topicService.getAllTopic();
    }

    @PostMapping("/create-topic")
    public ResponseVO createTopic(@RequestBody TopicDto topic) {
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
