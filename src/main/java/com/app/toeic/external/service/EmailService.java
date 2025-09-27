package com.app.toeic.external.service;

import com.app.toeic.external.payload.EmailDto;
import com.app.toeic.user.payload.LoginSocialDto;
import com.app.toeic.external.response.ResponseVO;

public interface EmailService {
    String generateOTP();

    ResponseVO sendEmail(EmailDto emailDto, String templateName);

    Object sendEmailAccount(LoginSocialDto loginSocialDto, String password, String templateName);
}
