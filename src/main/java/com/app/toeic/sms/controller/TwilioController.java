package com.app.toeic.sms.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.sms.service.TwilioService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twilio")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TwilioController {
    TwilioService twilioService;
    @GetMapping("verify")
    public Object verifyPhoneNumber(@RequestParam("phone") String phoneNumber) {
        twilioService.verifyPhoneNumber(phoneNumber);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("SEND_SMS_SUCCESS")
                .build();
    }
}
