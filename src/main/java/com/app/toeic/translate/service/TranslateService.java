package com.app.toeic.translate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TranslateService {
    @Value("${GOOGLE_TRANSLATE_API}")
    String googleTranslationUrl;

    private final RestTemplate restTemplate;

    public Object translate(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        headers.set("User-Agent", "AndroidTranslate/5.3.0.RC02.130475354-53000263 5.1 phone TRANSLATE_OPM5_TEST_1");

        var map = new LinkedMultiValueMap<>();
        map.add("sl", "en-us");
        map.add("tl", "vi-vn");
        map.add("q", content);

        var entity = new HttpEntity<>(map, headers);
        var response = restTemplate.exchange(googleTranslationUrl, HttpMethod.POST, entity, Object.class);
        return response.getBody();
    }

}
