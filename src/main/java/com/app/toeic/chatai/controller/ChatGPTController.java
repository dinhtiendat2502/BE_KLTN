package com.app.toeic.chatai.controller;

import com.app.toeic.chatai.model.Message;
import com.app.toeic.chatai.payload.ChatRequest;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatGPTController {
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @PostMapping("/chat")
    public Object chat(@RequestBody ChatRequestBody req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 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");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        req.getChatRequest().addMessage(new Message("user", req.getPrompt()));
        // create a request
        HttpEntity<ChatRequest> httpEntity = new HttpEntity<>(req.getChatRequest(), headers);
        ResponseEntity<ChatResponse> responseEntity = restTemplate.postForEntity(apiUrl, httpEntity, ChatResponse.class);
        req.getChatRequest().addMessage(new Message("system", Objects.requireNonNull(responseEntity.getBody()).getChoices().getFirst().getMessage().getContent()));
        return responseEntity.getBody();
    }
}
