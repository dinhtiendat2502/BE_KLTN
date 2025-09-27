package com.app.toeic.external.service;

import com.app.toeic.external.payload.EmailDTO;
import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.external.response.ResponseVO;

public interface EmailService {
    String generateOTP();

    ResponseVO sendEmail(EmailDTO emailDto, String templateName);

    Object sendEmailAccount(LoginSocialDTO loginSocialDto, String password, String templateName);
}
