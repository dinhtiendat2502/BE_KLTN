package com.app.toeic.config;


import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {
    private static final int DEFAULT_TIMEOUT = 60_000;
    @Bean
    RestTemplate restTemplate() {
        var restTemplate = new RestTemplate(getRequestFactory());
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            var headers = request.getHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

    private ClientHttpRequestFactory getRequestFactory() {
        var config = RequestConfig.custom()
                                  .setConnectionRequestTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                                  .setResponseTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                                  .build();
        var client = HttpClients.custom().setDefaultRequestConfig(config).build();
        var defaultRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        defaultRequestFactory.setConnectTimeout(DEFAULT_TIMEOUT);
        defaultRequestFactory.setConnectionRequestTimeout(DEFAULT_TIMEOUT);
        return defaultRequestFactory;
    }
}
