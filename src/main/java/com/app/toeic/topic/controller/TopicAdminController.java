package com.app.toeic.topic.controller;

import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.topic.service.TopicService;
import com.app.toeic.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/topic")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TopicAdminController {
    TopicService topicService;
    FirebaseStorageService firebaseStorageService;

    @GetMapping("/list")
    public Object getAllTopic(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return topicService.getAllTopic(page, size);
    }

    @PostMapping(value = "/create-topic", consumes = {"multipart/form-data"})
    public ResponseVO createTopic(
            @RequestParam("topicName") String topicName,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var image = firebaseStorageService.uploadFile(file);
        var newTopic = Topic
                .builder()
                .topicName(topicName)
                .topicImage(image)
                .status(Constant.STATUS_ACTIVE)
                .build();
        topicService.saveTopic(newTopic);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CREATE_TOPIC_SUCCESS")
                .build();
    }

    @PatchMapping(value = "/update/{topicId}", consumes = {"multipart/form-data"})
    public ResponseVO updateTopic(
            @PathVariable("topicId") Integer topicId,
            @RequestParam("topicName") String topicName,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        var image = "";
        if(file != null) {
            image = firebaseStorageService.uploadFile(file);
        }

        var topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new RuntimeException("TOPIC_NOT_FOUND"));
        topic.setTopicName(StringUtils.defaultIfBlank(topicName, topic.getTopicName()));
        topic.setTopicImage(StringUtils.defaultIfBlank(image, topic.getTopicImage()));
        topicService.saveTopic(topic);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_TOPIC_SUCCESS")
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
