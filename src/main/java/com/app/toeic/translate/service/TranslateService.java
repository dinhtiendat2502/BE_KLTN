package com.app.toeic.translate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranslateService {
    @Value("${GOOGLE_TRANSLATE_API}")
    String googleTranslationUrl;

    final RestTemplate restTemplate;

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
        var resultBuilder = new StringBuilder();
        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            if (body instanceof Map<?, ?> map1) {
                if (map1.containsKey("sentences")) {
                    var sentences = (List<?>) map1.get("sentences");
                    for (Object sentence : sentences) {
                        if (sentence instanceof Map<?, ?> sentenceMap) {
                            if (sentenceMap.containsKey("trans")) {
                                var trans = (String) sentenceMap.get("trans");
                                resultBuilder.append(trans.trim()).append("\n");
                            }
                        }
                    }
                }
            }
        }
        log.info(MessageFormat.format("TranslateService >> translate >> content: {0}, response: {1}", content, resultBuilder.toString()));
        return resultBuilder.toString();
    }

}
