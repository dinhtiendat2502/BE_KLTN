package com.app.toeic.config;

import com.app.toeic.chatai.repo.ChatAiRepository;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.util.HttpStatus;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.Transport;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeminiConfig {
    ChatAiRepository chatAiRepository;

    @Bean
    public VertexAI vertexAI() throws IOException {
        var chatAiConfig = chatAiRepository.findAllByStatusAndType(true, "GEMINI");
        if (CollectionUtils.isEmpty(chatAiConfig) || chatAiConfig.getFirst() == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "GEMINI_CONFIG_NOT_FOUND");
        }
        var config = chatAiConfig.getFirst();
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(config.getToken().getBytes()));
        return new VertexAI
                .Builder()
                .setLocation(config.getLocation())
                .setProjectId(config.getProjectId())
                .setCredentials(credentials)
                .setTransport(Transport.REST)
                .build();
    }

    @Bean
    public GenerativeModel geminiProGenerativeModel(VertexAI vertexAI) {
        return new GenerativeModel("gemini-pro", vertexAI);
    }
}
