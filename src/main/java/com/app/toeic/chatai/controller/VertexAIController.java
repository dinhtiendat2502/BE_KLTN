package com.app.toeic.chatai.controller;

import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.external.response.ResponseVO;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("vertex")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VertexAIController {
    private final ChatAiRepository chatAiRepository;

    @PostMapping("get")
    public Object startChat() throws IOException {
        var chatAiConfig = chatAiRepository.findAllByStatusAndType(true, "GEMINI");
        if (CollectionUtils.isEmpty(chatAiConfig) || chatAiConfig.getFirst() == null) {
            return ResponseVO
                    .builder()
                    .success(true)
                    .message("NOT_FOUND_CHAT_AI_CONFIG")
                    .build();
        }
        var config = chatAiConfig.getFirst();
        try (VertexAI vertexAi = new VertexAI(config.getProjectId(), config.getLocation())) {
            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);
            GenerateContentResponse response = model.generateContent("How are you?");
            return ResponseVO
                    .builder()
                    .success(true)
                    .data(response)
                    .build();
        } catch (Exception e) {
            return ResponseVO
                    .builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }
}
