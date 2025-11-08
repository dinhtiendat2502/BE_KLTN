package com.app.toeic.chatai.controller;

import com.app.toeic.external.response.ResponseVO;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("gemini")
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeminiController {
    GenerativeModel generativeModel;

    @PostMapping("ask/v1")
    public Object askGeminiV1(@RequestBody GeminiPayload payload) {

        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @PostMapping("ask/v2")
    public Object startChat(@RequestBody GeminiPayload payload) {
        var chatSession = new ChatSession(generativeModel);
        var historySession = payload.listMessage.stream().map(ContentMaker::fromString).toList();
        chatSession.setHistory(historySession);
        try {
            var response = chatSession.sendMessage(payload.newMessage);
            var text = ResponseHandler.getText(response);
            return ResponseVO
                    .builder()
                    .data(text)
                    .success(true)
                    .message("ASK_GEMINI_SUCCESS")
                    .build();
        } catch (IOException ex) {
            log.log(
                    Level.WARNING,
                    MessageFormat.format("VertexAIController >> startChat >> body: {0}", payload.listMessage),
                    ex
            );
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("FAIL_ASK_GEMINI")
                    .build();
        }
    }

    public record GeminiPayload(List<String> listMessage, String newMessage) {
    }
}
