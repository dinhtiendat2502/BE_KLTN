package com.app.toeic.chatai.controller;

import com.app.toeic.chatai.model.ChatAI;
import com.app.toeic.chatai.payload.Message;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.payload.TextToSpeechPayload;
import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.response.ResponseVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("chat-gpt")
public class ChatGPTController {
    RestTemplate restTemplate;
    ChatAiRepository chatAiRepository;

    @PostMapping("/chat")
    public Object chat(@RequestBody ChatRequestBody req) {
        var chatGptConfig = chatAiRepository.findAllByStatusAndType(true, "GPT").getFirst();
        HttpHeaders headers = createHeaders(chatGptConfig);
        req.getChatRequest().addMessage(new Message("user", chatGptConfig.getPrompt().formatted(req.getPrompt())));
        var httpEntity = new HttpEntity<>(req.getChatRequest(), headers);
        var responseEntity = restTemplate.postForEntity(
                chatGptConfig.getUrl(),
                httpEntity,
                ChatResponse.class
        );
        return responseEntity.getBody();
    }

    @PostMapping("text-to-speech")
    public Object textToSpeech(@RequestBody TextToSpeechPayload payload) {
        var chatGptConfig = chatAiRepository.findAllByStatusAndType(true, "GPT").getFirst();
        String url = "https://api.openai.com/v1/audio/speech";
        var headers = createHeaders(chatGptConfig);
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

    private HttpHeaders createHeaders(ChatAI config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(config.getToken());
        return headers;
    }
}
