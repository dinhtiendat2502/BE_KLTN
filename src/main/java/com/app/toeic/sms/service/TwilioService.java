package com.app.toeic.sms.service;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TwilioService {
    SystemConfigService systemConfigService;

    @PostConstruct
    public void init() {
        updateConfig();
        log.info("TwilioService >> init");
    }

    public void updateConfig(){
        var twilioSid = systemConfigService.getConfigValue(Constant.TWILIO_SID);
        var twilioToken = systemConfigService.getConfigValue(Constant.TWILIO_TOKEN);
    }
    public void sendSMS(String to) {

    }
    public void verifyPhoneNumber(String phoneNumber) {
        log.info("TwilioService >> verifyPhoneNumber >> phoneNumber: " + phoneNumber);
    }
}
