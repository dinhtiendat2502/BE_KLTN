package com.app.toeic.chatai.controller;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ChatAI;
import com.app.toeic.chatai.model.ModelChat;
import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.payload.AskDTO;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.Constant;
import com.app.toeic.util.JsonConverter;
import com.app.toeic.util.RequestUtils;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("llm")
public class LLamasController {
    RestTemplate restTemplate;
    ChatAiRepository chatAiRepository;

    @PostMapping("ask")
    @ChatAiLog(model = ModelChat.LLAMAS)
    public Object ask(@RequestBody String input) {
        var config = getChatAI();
        var result = ResponseVO
                .builder()
                .build();
        try {
            var headers = RequestUtils.createHeaders();
            var httpEntity = new HttpEntity<>(Map.of("input", input), headers);
            var responseEntity = restTemplate.postForEntity(
                    config.getUrl(),
                    httpEntity,
                    Object.class
            );
            result.setData(responseEntity.getBody());
            result.setSuccess(true);
        } catch (Exception e) {
            log.log(
                    Level.WARNING,
                    MessageFormat.format("LLamasController >> ask >> param: {0} >> Exception: ", input),
                    e
            );
            result.setSuccess(false);
        }
        return result;
    }

    private ChatAI getChatAI() {
        return chatAiRepository.findAllByStatusAndType(true, Constant.LLAMAS).getFirst();
    }
}
