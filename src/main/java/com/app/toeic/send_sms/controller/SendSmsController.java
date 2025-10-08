package com.app.toeic.send_sms.controller;

import com.app.toeic.chatai.model.Message;
import com.app.toeic.chatai.payload.ChatRequest;
import com.app.toeic.chatai.response.ChatResponse;
import com.app.toeic.send_sms.payload.Sms;
import com.app.toeic.send_sms.payload.SmsPayload;
import com.app.toeic.send_sms.payload.SmsText;
import com.app.toeic.send_sms.response.SmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SendSmsController {
    RestTemplate restTemplate;

    @PostMapping("send")
    public Object sendSms(@RequestBody String phoneTo) {
        var payload = SmsPayload.builder().build();
        payload.getSms().add(
                Sms.builder()
                        .to(phoneTo)
                        .from("GSMS")
                        .text(SmsText.builder()
                                .template(5689)
                                .params(new String[]{"123456"})
                                .build())
                        .build()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set(
                "X-STRINGEE-AUTH",
                "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLnFueUxHSkVZVmZoZmkwVEpPMXVFeVg2ZlFyTVZzUmw3LTE3MTMxNjgwMTEiLCJpc3MiOiJTSy4wLnFueUxHSkVZVmZoZmkwVEpPMXVFeVg2ZlFyTVZzUmw3IiwiZXhwIjoxNzEzMjU0NDExLCJyZXN0X2FwaSI6dHJ1ZX0.NX65uCNIUI9pZ_WC8J0GYCU1cJVNRfKtv5GpDsXH12k"
        );
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // create a request
        HttpEntity<SmsPayload> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "https://api.stringee.com/v1/sms",
                httpEntity,
                Object.class
        );
        return responseEntity.getBody();
    }

    private String getAccessToken() {
        long now = System.currentTimeMillis() / 1000;
        long exp = now + 3600;

        String apiKeySecret = "bU5qNUFydXBuWElsdTV5T1Zqb2dkaW5KZWkzZDhKYg==";
        Key key = Keys.hmacShaKeyFor(apiKeySecret.getBytes());

        String apiKeySid = "SK.0.qnyLGJEYVfhfi0TJO1uEyX6fQrMVsRl7";
        return Jwts.builder()
                .claim("jti", STR."\{apiKeySid}-\{now}")
                .claim("iss", apiKeySid)
                .claim("exp", exp)
                .claim("rest_api", 1)
                .setHeaderParam("cty", "stringee-api;v=1")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
