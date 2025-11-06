package com.app.toeic.chatai.controller;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ModelChat;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;


@Log
@RestController
@RequestMapping("vertex")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VertexAIController {
    VertexAiPaLm2ChatClient vertexAiPaLm2ChatClient;
    VertexAiGeminiChatClient vertexAiGeminiChatClient;

    @PostMapping("/gemini/ask")
    @ChatAiLog(model = ModelChat.GEMINI)
    public Object generate(@RequestBody PayloadVertex payloadVertex) {
        var rs = ResponseVO
                .builder()
                .success(true)
                .build();
        try {
            var listMessage = new java.util.ArrayList<>(convertToMessage(payloadVertex.listMsg()));
            listMessage.add(new UserMessage(payloadVertex.prompt()));
            var prompt = new Prompt(listMessage);
            var chatResponse = vertexAiGeminiChatClient.call(prompt);
            var result = chatResponse.getResult().getOutput().getContent();
            rs.setData(result);
        } catch (Exception e) {
            log.log(Level.WARNING, "VertexAIController >> generate >> Exception: ", e);
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("/palm2/ask")
    @ChatAiLog(model = ModelChat.PALM2)
    public Object generatePalm2(@RequestBody String text) {
        var prompt = new Prompt(new UserMessage(text));
        var rs = ResponseVO
                .builder()
                .success(true)
                .build();
        try {
            var result = vertexAiPaLm2ChatClient.call(prompt).getResult().getOutput().getContent();
            rs.setData(result);
        } catch (Exception e) {
            log.log(Level.WARNING, "VertexAIController >> generatePalm2 >> Exception: ", e);
            rs.setSuccess(false);
        }
        return rs;
    }

    private List<Message> convertToMessage(List<PayloadVertex.Msg> listMsg) {
        return listMsg.stream()
                      .map(msg -> {
                          if (Constant.USER.equalsIgnoreCase(msg.type())) {
                              return new UserMessage(msg.text());
                          }
                          return (Message) new AssistantMessage(msg.text());
                      })
                      .toList();
    }

    public record PayloadVertex(List<Msg> listMsg, String prompt) {
        public record Msg(String text, String type) {
        }
    }
}
