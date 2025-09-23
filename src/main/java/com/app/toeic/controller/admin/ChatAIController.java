package com.app.toeic.controller.admin;


import com.app.toeic.dto.AskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/chat-ai")
public class ChatAIController {
    private final RestTemplate restTemplate;
    @Value("${BOT_AI_URL}")
    private String apiUrl;

    @PostMapping("ask")
    public Object ask(@RequestBody AskDTO ask) {
        return restTemplate.postForObject(apiUrl, ask, Object.class);
    }

}
