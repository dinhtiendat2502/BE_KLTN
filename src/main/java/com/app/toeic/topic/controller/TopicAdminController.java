package com.app.toeic.topic.controller;

import com.app.toeic.external.service.FirebaseStorageService;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/topic")
public class TopicAdminController {
    private final TopicService topicService;
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/list")
    public Object getAllTopic(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return topicService.getAllTopic(page, size);
    }

    @PostMapping("/create-topic")
    public ResponseVO createTopic(
            @RequestParam("topicName") String topicName,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var image = firebaseStorageService.uploadFile(file);
        var newTopic = Topic
                .builder()
                .topicName(topicName)
                .topicImage(image)
                .status("ACTIVE")
                .build();
        topicService.addTopic(newTopic);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CREATE_TOPIC_SUCCESS")
                .build();
    }

    @DeleteMapping("/delete/{topicId}")
    public ResponseVO deleteTopic(@PathVariable("topicId") Integer topicId) {
        topicService.removeTopic(topicId);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_TOPIC_SUCCESS")
                .build();
    }
}
