package com.app.toeic.stringee.controller;


import com.app.toeic.stringee.payload.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/call")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CallController {
    RestTemplate restTemplate;

    @PostMapping("make-call")
    public Object sendSms(
            @RequestParam("phoneTo") String phoneTo,
            @RequestParam("msg") String msg
    ) {
        var from = CallFromPayload
                .builder()
                .type("external")
                .number("842471005352")
                .alias("toeicute-tester")
                .build();
        var to = List.of(
                CallToPayload
                        .builder()
                        .type("external")
                        .number(phoneTo)
                        .alias("kira")
                        .build()
        );
        var actions = List.of(
                CallActionPayload
                        .builder()
                        .action("talk")
                        .text(msg)
                        .build()
        );
        var payload = CallPayload
                .builder()
                .from(from)
                .to(to)
                .actions(actions)
                .answer_url("http://v2.stringee.com:8282/project_answer_url")
                .build();


        HttpHeaders headers = getHttpHeaderCall();
        // create a request
        HttpEntity<CallPayload> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
                "https://api.stringee.com/v1/call2/callout",
                httpEntity,
                Object.class
        );
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaderCall() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(
                "X-STRINGEE-AUTH",
                "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTSy4wLnFueUxHSkVZVmZoZmkwVEpPMXVFeVg2ZlFyTVZzUmw3LTE3MTMyNTYzOTQiLCJpc3MiOiJTSy4wLnFueUxHSkVZVmZoZmkwVEpPMXVFeVg2ZlFyTVZzUmw3IiwiZXhwIjoxNzEzMzQyNzk0LCJyZXN0X2FwaSI6dHJ1ZX0.rTWqc02mf_K1SAWbvpNhuXs1U1tO86E-AiyvKNGiZw8"
        );
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
