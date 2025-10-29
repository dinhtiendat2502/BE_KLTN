package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.ChatAiLog;
import com.app.toeic.chatai.model.ChatHistory;
import com.app.toeic.chatai.payload.ChatRequestBody;
import com.app.toeic.chatai.repo.ChatHistoryRepository;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
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
            if (args.length > 0 && args[0] instanceof ChatRequestBody requestBody) {
                chatHistory.setQuestion(requestBody.getPrompt());
            }
            var rs = pjp.proceed();
            var currentUser = userService.getCurrentUser();
            currentUser.ifPresent(chatHistory::setUser);
            if(rs instanceof ChatResponse chatResponse) {
                chatHistory.setAnswer(chatResponse.getChoices().getFirst().getMessage().getContent());
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
