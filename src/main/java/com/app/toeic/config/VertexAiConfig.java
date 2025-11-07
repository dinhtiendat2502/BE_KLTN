package com.app.toeic.config;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vertexai.VertexAI;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatClient;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatOptions;
import org.springframework.ai.vertexai.palm2.api.VertexAiPaLm2Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VertexAiConfig {
    SystemConfigService systemConfigService;

    @Lazy
    @Bean
    public OpenAiChatClient openAiChatClient() {
        var apiKey = systemConfigService.getConfigValue(Constant.GPT_4);
        var openAiApi = new OpenAiApi(apiKey);
        return new OpenAiChatClient(
                openAiApi,
                OpenAiChatOptions
                        .builder()
                        .withModel(Constant.MODEL_GPT)
                        .withTemperature(0.6F)
                        .withMaxTokens(400)
                        .build()
        );
    }

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
                                         .withModel(VertexAiGeminiChatClient.ChatModel.GEMINI_PRO.value)
                                         .withTemperature(1F)
                                         .withTopP(0.95F)
                                         .withMaxOutputTokens(8192)
                                         .build()
        );
    }

    @Lazy
    @Bean
    public VertexAiPaLm2ChatClient vertexAiPaLm2ChatClient() {
        var apiKey = systemConfigService.getConfigValue(Constant.PALM2);
        var vertexApi = new VertexAiPaLm2Api(apiKey);
        return new VertexAiPaLm2ChatClient(
                vertexApi,
                VertexAiPaLm2ChatOptions.builder()
                                        .withTemperature(0.95F)
                                        .build()
        );
    }
}
