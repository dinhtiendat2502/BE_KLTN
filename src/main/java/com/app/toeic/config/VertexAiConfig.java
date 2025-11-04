package com.app.toeic.config;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vertexai.Transport;
import com.google.cloud.vertexai.VertexAI;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VertexAiConfig {
    SystemConfigService systemConfigService;

    @Lazy
    @Bean
    public VertexAiGeminiChatClient vertexAiGeminiChatClient() throws IOException {
        var jsonContent = systemConfigService.getConfigValue(Constant.VERTEX);
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()))
                                           .createScoped(
                                                   "https://www.googleapis.com/auth/cloud-platform",
                                                   "https://www.googleapis.com/auth/cloud-platform.read-only"
                                           );
        var vertexApi = new VertexAI("beaming-inn-423802-q6", "us-central1", credentials);
        return new VertexAiGeminiChatClient(
                vertexApi,
                VertexAiGeminiChatOptions.builder()
                                         .withModel("gemini-1.0-pro")
                                         .withTemperature(1F)
                                         .withTopP(0.95F)
                                         .withMaxOutputTokens(8192)
                                         .build()
        );
    }
}
