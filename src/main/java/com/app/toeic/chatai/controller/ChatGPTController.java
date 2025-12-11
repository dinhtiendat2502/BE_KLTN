package com.app.toeic.chatai.controller;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ChatAI;
import com.app.toeic.chatai.model.ModelChat;
import com.app.toeic.chatai.payload.Message;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.payload.TextToSpeechPayload;
import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.Constant;
import com.app.toeic.util.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("gpt")
public class ChatGPTController {
    RestTemplate restTemplate;
    ChatAiRepository chatAiRepository;
    OpenAiChatClient openAiChatClient;

    @PostMapping("ask")
    @ChatAiLog(model = ModelChat.GPT)
    public Object ask(@RequestBody PayloadOpenAi payloadOpenAi) {
        var rs = ResponseVO
                .builder()
                .success(true)
                .build();
        try {
            var listMessage = new java.util.ArrayList<>(convertToMessage(payloadOpenAi.listMsg()));
            listMessage.add(new UserMessage(payloadOpenAi.prompt()));
            var prompt = new Prompt(listMessage);
            var text = openAiChatClient.call(prompt).getResult().getOutput().getContent();
            rs.setData(text);
        } catch (Exception e) {
            log.log(
                    Level.WARNING,
                    MessageFormat.format(
                            "ChatGPTController >> ask >> param: {0}",
                            JsonConverter.convertObjectToJson(payloadOpenAi)
                    ),
                    e
            );
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("/chat")
    @ChatAiLog(model = ModelChat.GPT)
    public Object chat(@RequestBody ChatRequestBody req) {
        var chatGptConfig = chatAiRepository.findAllByStatusAndType(true, Constant.GPT).getFirst();
        var headers = createHeaders(chatGptConfig);
        req.getChatRequest().addMessage(new Message("user", chatGptConfig.getPrompt().formatted(req.getPrompt())));
        var httpEntity = new HttpEntity<>(req.getChatRequest(), headers);
        var responseEntity = restTemplate.postForEntity(
                chatGptConfig.getUrl(),
                httpEntity,
                ChatResponse.class
        );
        return ResponseVO
                .builder()
                .success(true)
                .data(responseEntity.getBody())
                .build();
    }

    @PostMapping("text-to-speech")
    public Object textToSpeech(@RequestBody TextToSpeechPayload payload) {
        var chatGptConfig = chatAiRepository.findAllByStatusAndType(true, Constant.GPT).getFirst();
        var url = "https://api.openai.com/v1/audio/speech";
        var headers = createHeaders(chatGptConfig);
        payload.setModel("tts-1");
        var httpEntity = new HttpEntity<>(payload, headers);
        var responseEntity = restTemplate.postForEntity(url, httpEntity, byte[].class);
        var audioData = responseEntity.getBody();
        final var rs = new String[2];
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
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(config.getToken());
        return headers;
    }

    private List<org.springframework.ai.chat.messages.Message> convertToMessage(List<PayloadOpenAi.Msg> listMsg) {
        return listMsg.stream()
                      .map(msg -> {
                          if (Constant.USER.equalsIgnoreCase(msg.type())) {
                              return new UserMessage(msg.text());
                          }
                          return (org.springframework.ai.chat.messages.Message) new AssistantMessage(msg.text());
                      })
                      .toList();
    }

    public record PayloadOpenAi(List<Msg> listMsg, String prompt) {
        public record Msg(String text, String type) {
        }
    }
}
