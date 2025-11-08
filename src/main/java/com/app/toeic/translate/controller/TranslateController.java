package com.app.toeic.translate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class TranslateController {
    @Value("${GOOGLE_TRANSLATE_API}")
    String googleTranslationUrl;

    private final RestTemplate restTemplate;

    @PostMapping("translate")
    public Object translate(@RequestBody String content) {
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
