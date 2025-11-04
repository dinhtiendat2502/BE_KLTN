package com.app.toeic.translate.service;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslateService {
    RestTemplate restTemplate;
    SystemConfigService systemConfigService;

    public Object translate(String content) {
        var googleTranslateUrl = systemConfigService.getConfigValue(Constant.GOOGLE_TRANSLATE_URL);
        if (StringUtils.isBlank(googleTranslateUrl)) {
            log.warning("TranslateService >> translate >> googleTranslateUrl is blank");
            return StringUtils.EMPTY;
        }
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        headers.set(Constant.USER_AGENT, Constant.ANDROID_DEVICE);

        var map = new LinkedMultiValueMap<>();
        map.add(Constant.SOURCE_LANGUAGE, Constant.EN_US);
        map.add(Constant.TARGET_LANGUAGE, Constant.VI_VN);
        map.add(Constant.CONTENT, content);

        var entity = new HttpEntity<>(map, headers);
        var response = restTemplate.exchange(googleTranslateUrl, HttpMethod.POST, entity, Object.class);
        var resultBuilder = new StringBuilder();
        getResponseTranslate(response, resultBuilder);
        log.info(MessageFormat.format(
                "TranslateService >> translate >> content: {0}, response: {1}",
                content,
                resultBuilder
        ));
        return resultBuilder.toString();
    }

    private void getResponseTranslate(ResponseEntity<Object> response, StringBuilder resultBuilder) {
        if (response.getStatusCode().is2xxSuccessful()) {
            var body = response.getBody();
            if (body instanceof Map<?, ?> map1 && (map1.containsKey("sentences"))) {
                    var sentences = (List<?>) map1.get("sentences");
                    for (Object sentence : sentences) {
                        if (sentence instanceof Map<?, ?> sentenceMap && (sentenceMap.containsKey("trans"))) {
                                var trans = (String) sentenceMap.get("trans");
                                resultBuilder.append(trans.trim()).append("\n");

                        }
                    }

            }
        }
    }
}
