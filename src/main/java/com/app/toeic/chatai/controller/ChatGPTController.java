package com.app.toeic.chatai.controller;

import com.app.toeic.chatai.payload.Message;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.payload.TextToSpeechPayload;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.response.ResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChatGPTController {
    private final RestTemplate restTemplate;
    private final OpenAiChatClient openAiChatClient;
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;


    @PostMapping("chat-gpt-v2")
    public Object chatGPTV2(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", openAiChatClient.call(message));
    }


    @PostMapping("/chat")
    public Object chat(@RequestBody ChatRequestBody req) {
        HttpHeaders headers = createHeaders();
        req.getChatRequest().addMessage(new Message("user", req.getPrompt()));
        // create a request
        var httpEntity = new HttpEntity<>(req.getChatRequest(), headers);
        var responseEntity = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                ChatResponse.class
        );
        return responseEntity.getBody();
    }

    @PostMapping("text-to-speech")
    public Object textToSpeech(@RequestBody TextToSpeechPayload payload) {
        String url = "https://api.openai.com/v1/audio/speech";
        var headers = createHeaders();
        payload.setModel("tts-1");
        var httpEntity = new HttpEntity<>(payload, headers);
        var responseEntity = restTemplate.postForEntity(url, httpEntity, byte[].class);
        var audioData = responseEntity.getBody();
        final String[] rs = new String[2];
        if (audioData != null) {
            try (var fos = new FileOutputStream("audio.mp3")) {
                fos.write(audioData);
                rs[0] = "SUCCESS";
                rs[1] = "audio.mp3";
            } catch (IOException ex) {
                rs[0] = "ERROR";
                rs[1] = ex.getMessage();
            }
        }
        return ResponseVO
                .builder()
                .message(rs[0])
                .data(rs[1])
                .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("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");
        return headers;
    }
}
