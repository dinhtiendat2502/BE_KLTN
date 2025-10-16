package com.app.toeic.config;


import ai.rev.speechtotext.ApiClient;
import com.app.toeic.revai.model.RevAIConfig;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RevAiConfig {
    RevAIConfigRepo revAIConfigRepo;

    @Bean
    ApiClient getApiClient() {
        var revAiConfig = revAIConfigRepo.findAllByStatus(true).stream().findFirst().orElse(new RevAIConfig());
        return new ApiClient(revAiConfig.getAccessToken());
    }
}
