package com.app.toeic.stringee.controller;

import com.app.toeic.stringee.payload.Sms;
import com.app.toeic.stringee.payload.SmsPayload;
import com.app.toeic.stringee.payload.SmsText;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.List;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SendSmsController {
    RestTemplate restTemplate;

    @PostMapping("send")
    public Object sendSms(
            @RequestParam("phoneTo") String phoneTo,
            @RequestParam("msg") String msg,
            @RequestParam("auth") String auth
    ) {
        var payload = SmsPayload.builder().build();
        payload.getSms().add(
                Sms.builder()
                        .to(phoneTo)
                        .from("842471005352")
                        .text(msg)
                        .build()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.set(
                "X-STRINGEE-AUTH",
                auth
        );
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // create a request
        HttpEntity<SmsPayload> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "https://api.stringee.com/v1/sms/send",
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
