package com.app.toeic.chatai.controller;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ChatAI;
import com.app.toeic.chatai.model.ModelChat;
import com.app.toeic.chatai.payload.GeminiPayload;
import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.JsonConverter;
import com.app.toeic.util.RequestUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
@RequestMapping("vertex")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VertexAIController {
    VertexAiPaLm2ChatClient vertexAiPaLm2ChatClient;
    VertexAiGeminiChatClient vertexAiGeminiChatClient;
    RestTemplate restTemplate;
    ChatAiRepository chatAiRepository;

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
        var config = getChatAI();
        var partContent = new GeminiPayload.PartContent(text);
        var content = new GeminiPayload.Content(Constant.USER, List.of(partContent));
        var payload = GeminiPayload.builder()
                .contents(List.of(content))
                .build();
        var result = ResponseVO.builder().success(false).build();
        try {
            var headers = RequestUtils.createHeaders();
            var httpEntity = new HttpEntity<>(payload, headers);
            var responseEntity = restTemplate.postForEntity(
                    MessageFormat.format("{0}{1}", config.getUrl(), config.getToken()),
                    httpEntity,
                    Object.class
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful() // if response is not successful
                    || !(responseEntity.getBody() instanceof Map<?, ?> m) // or response body is not a map
                    || !(m.get("candidates") instanceof List<?> l) // or candidates is not a list
                    || !(l.getFirst() instanceof Map<?, ?> m2)  // or first element of candidates is not a map
                    || !(m2.get("content") instanceof Map<?, ?> c) // or content of first element of candidates is not a map
                    || !(c.get("parts") instanceof List<?> lp && CollectionUtils.isNotEmpty(lp)) // or parts of content is not a list
                    || !(lp.getFirst() instanceof Map<?, ?> m3) // or first element of parts is not a map
            ) {
                return result;
            }

            result.setData(m3.get("text"));
            result.setSuccess(true);

            return result;
        } catch (Exception e) {
            log.log(
                    Level.WARNING,
                    MessageFormat.format(
                            "GeminiController >> ask >> param: {0} >> Exception: ",
                            JsonConverter.convertObjectToJson(payload)
                    ),
                    e
            );
            return result;
        }
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
        public GeminiPayload convert() {
            GeminiPayload builder = new GeminiPayload();
            builder.setContents(new java.util.ArrayList<>());

            if (listMsg() != null) {
                for (PayloadVertex.Msg msg : listMsg()) {
                    GeminiPayload.Content content = GeminiPayload.Content.builder()
                            .role(msg.type())  // thường là "user" hoặc "model" tùy use case
                            .parts(List.of(
                                    GeminiPayload.PartContent.builder()
                                            .text(msg.text())
                                            .build()
                            ))
                            .build();
                    builder.getContents().add(content);
                }
            }

            if (prompt() != null && !prompt().isBlank()) {
                GeminiPayload.Content promptContent = GeminiPayload.Content.builder()
                        .role("user")
                        .parts(List.of(
                                GeminiPayload.PartContent.builder()
                                        .text(prompt())
                                        .build()
                        ))
                        .build();

                builder.getContents().add(promptContent);
            }

            return builder;
        }
    }
    private ChatAI getChatAI() {
        return chatAiRepository.findAllByStatusAndType(true, Constant.GEMINI_CURL).getFirst();
    }
}
