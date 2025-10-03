package com.app.toeic.email.service;

import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.external.response.ResponseVO;

public interface EmailService {
    String generateOTP();

    void sendEmail(String emailTo, String templateCode);

    void sendEmailAccount(LoginSocialDTO loginSocialDto, String password, String templateName);
}
