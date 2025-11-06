package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.controller.ChatGPTController;
import com.app.toeic.chatai.controller.VertexAIController;
import com.app.toeic.chatai.model.ChatHistory;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.payload.GeminiPayload;
import com.app.toeic.chatai.repo.ChatHistoryRepository;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatAiLogAspect {
    UserService userService;
    ChatHistoryRepository chatHistoryRepository;

    @Around(value = "@annotation(chatAiLog)", argNames = "pjp, chatAiLog")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final ChatAiLog chatAiLog) throws Throwable {
        var startTime = System.currentTimeMillis();
        try {
            var args = pjp.getArgs();
            var chatHistory = ChatHistory
                    .builder()
                    .model(chatAiLog.model().name())
                    .build();
            if (args.length > 0) {
                switch (args[0]) {
                    case ChatGPTController.PayloadOpenAi chatRequestBody -> chatHistory.setQuestion(chatRequestBody.prompt());
                    case String input -> chatHistory.setQuestion(input);
                    case VertexAIController.PayloadVertex geminiPayload -> chatHistory.setQuestion(geminiPayload.prompt());
                    default -> log.log(
                            Level.WARNING,
                            MessageFormat.format("ChatAiLogAspect >> aroundAspect >> Unknown args: {0}", args[0])
                    );
                }
            }
            var rs = pjp.proceed();
            var currentUser = userService.getCurrentUser();
            currentUser.ifPresent(chatHistory::setUser);
            if (rs instanceof ResponseVO rsVO) {
                switch (rsVO.getData()) {
                    case ChatResponse chatResponse ->
                            chatHistory.setAnswer(chatResponse.getChoices().getFirst().getMessage().getContent());
                    case String answer -> chatHistory.setAnswer(answer);
                    case Map<?, ?> m -> chatHistory.setAnswer(JsonConverter.convertObjectToJson(m));
                    default -> log.log(
                            Level.WARNING,
                            MessageFormat.format(
                                    "ChatAiLogAspect >> aroundAspect >> Unknown rsVO.getData(): {0}",
                                    JsonConverter.convertObjectToJson(rsVO.getData())
                            )
                    );
                }
            }
            chatHistoryRepository.save(chatHistory);
            return rs;
        } catch (Exception e) {
            log.log(Level.WARNING, MessageFormat.format(
                    "ChatAiLogAspect >> aroundAspect >> Exception: {0}",
                    e
            ));
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("Error")
                    .build();
        } finally {
            log.info(MessageFormat.format(
                    "ChatAiLogAspect >> aroundAspect >> execute: {0} ms",
                    System.currentTimeMillis() - startTime
            ));
        }
    }
}
