package com.app.toeic.email.service;

import com.app.toeic.user.payload.LoginSocialDTO;

public interface EmailService {
    String generateOTP();

    void sendEmail(String emailTo, String templateCode);

    void sendEmailAccount(LoginSocialDTO loginSocialDto, String password, String templateName);
}
