package com.app.toeic.sms.service;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.twilio.rest.api.v2010.account.ValidationRequest;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.text.MessageFormat;

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

    public void updateConfig() {
        //        var twilioSid = systemConfigService.getConfigValue(Constant.TWILIO_SID);
        //        var twilioToken = systemConfigService.getConfigValue(Constant.TWILIO_TOKEN);
        var twilioSid = "AC5dd2b0c91c5f21b2e3847012113e812a";
        var twilioToken = "cbb77dd3ec48719e33adcf44cf446d66";
        Twilio.init(twilioSid, twilioToken);
    }

    public Object sendSMS(String to, String msg) {
        return Message
                .creator(
                        new PhoneNumber(to),
                        "MGfd13252a452f6a4ecb0d87eb47f20322",
                        MessageFormat.format(
                                "Dear Customer, The OTP for registering your account on is: {0}. Valid only for 30 mins",
                                msg
                        )
                )
                .create();
    }

    public Object addOutgoingCallerId(String phoneNumber) {
        return ValidationRequest
                .creator(new com.twilio.type.PhoneNumber(phoneNumber))
                .setFriendlyName("My Home Phone Number")
                .create();
    }

    public void verifyPhoneNumber(String phoneNumber) {
        log.info("TwilioService >> verifyPhoneNumber >> phoneNumber: " + phoneNumber);
    }
}
