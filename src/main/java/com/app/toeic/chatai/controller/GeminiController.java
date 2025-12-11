package com.app.toeic.chatai.controller;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ChatAI;
import com.app.toeic.chatai.model.ModelChat;
import com.app.toeic.chatai.payload.GeminiPayload;
import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.Constant;
import com.app.toeic.util.JsonConverter;
import com.app.toeic.util.RequestUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("gemini")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeminiController {
    RestTemplate restTemplate;
    ChatAiRepository chatAiRepository;

    @PostMapping("ask/v1")
    @ChatAiLog(model = ModelChat.GEMINI)
    public Object askGeminiV1(@RequestBody VertexAIController.PayloadVertex request) {
        var config = getChatAI();
        var result = ResponseVO
                .builder()
                .build();
        try {
            var headers = RequestUtils.createHeaders();
            headers.set("x-goog-api-key", config.getToken());
            var partContent = new GeminiPayload.PartContent(request.prompt());
            var content = new GeminiPayload.Content(Constant.USER, List.of(partContent));
            var payload = GeminiPayload.builder()
                    .contents(List.of(content))
                    .build();
            var httpEntity = new HttpEntity<>(payload, headers);
            var responseEntity = restTemplate.postForEntity(
                    config.getUrl(),
                    httpEntity,
                    Object.class
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()
                    && responseEntity.getBody() instanceof Map<?, ?> m) {
                var candidates = m.get("candidates");
                if (candidates instanceof List<?> l) {
                    if(l.getFirst() instanceof Map<?, ?> m2) {
                        if(m2.get("content") instanceof Map<?, ?> m3) {
                            if(m3.get("parts") instanceof List<?> l2) {
                                if(l2.getFirst() instanceof Map<?, ?> m4) {
                                    if(m4.get("text") instanceof String s) {
                                        result.setData(s);
                                        result.setSuccess(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.log(
                    Level.WARNING,
                    MessageFormat.format(
                            "GeminiController >> ask >> param: {0} >> Exception: ",
                            JsonConverter.convertObjectToJson(request)
                    ),
                    e
            );
            result.setSuccess(false);
        }
        return result;
    }

    private ChatAI getChatAI() {
        return chatAiRepository.findAllByStatusAndType(true, Constant.GEMINI_CURL).getFirst();
    }
}
