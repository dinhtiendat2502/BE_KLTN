package com.app.toeic.chatai.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Blob;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;


@Log
@RestController
@RequestMapping("vertex")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VertexAIController {
    SystemConfigService systemConfigService;
    VertexAiPaLm2ChatClient chatClient;
    VertexAiGeminiChatClient vertexAiGeminiChatClient;
    @PostMapping("ask/v1")
    public Object askVertexV1(@RequestBody PayloadVertex payloadVertex) {
//        var jsonContent = systemConfigService.getConfigValue(Constant.VERTEX);
//        var obj = ObjectUtils.CONST(null);
//        if (StringUtils.isBlank(jsonContent)) {
//            log.warning("VertexAIController >> askVertexV1 >> jsonContent is blank");
//            return ResponseVO
//                    .builder()
//                    .success(false)
//                    .message("VERTEX_CONFIG_NOT_FOUND")
//                    .build();
//        }
//        try (var vertexAi = new VertexAI.Builder()
//                .setProjectId("beaming-inn-423802-q6")
//                .setLocation("us-central1")
//                .setCredentials(ServiceAccountCredentials
//                                        .fromStream(new FileInputStream("src/main/resources/beaming-inn-423802-q6-329f5697e378.json"))
//                                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform")))
//                .build()) {
//            var generationConfig =
//                    GenerationConfig.newBuilder()
//                                    .setMaxOutputTokens(8192)
//                                    .setTemperature(1F)
//                                    .setTopP(0.95F)
//                                    .build();
//            var safetySettings = List.of(
//                    SafetySetting.newBuilder()
//                                 .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
//                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
//                                 .build(),
//                    SafetySetting.newBuilder()
//                                 .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
//                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
//                                 .build(),
//                    SafetySetting.newBuilder()
//                                 .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
//                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
//                                 .build(),
//                    SafetySetting.newBuilder()
//                                 .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
//                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
//                                 .build()
//            );
//            var model = new GenerativeModel.Builder()
//                    .setModelName("gemini-1.0-pro")
//                    .setVertexAi(vertexAi)
//                    .setGenerationConfig(generationConfig)
//                    .build();
//
//
////            var content = ContentMaker.fromMultiModalData(payloadVertex.prompt());
//
//            obj = model.generateContent("Hello, how are you?");
//        } catch (Exception e) {
//            log.warning("Error: " + e.getMessage());
//        }

        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @GetMapping("/ai/generate")
    public Object generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", vertexAiGeminiChatClient.call(message));
    }

    public record PayloadVertex(String prompt) {
    }
}
