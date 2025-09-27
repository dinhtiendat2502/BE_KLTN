package com.app.toeic.external.service.impl;


import com.app.toeic.external.payload.EmailDto;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.EmailService;
import com.app.toeic.user.payload.LoginSocialDto;
import com.app.toeic.exception.AppException;
import com.app.toeic.util.HttpStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final Random random = new Random();

    public String generateOTP() {
        int length = 6;
        String numbers = "0123456789";
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    @Override
    public ResponseVO sendEmail(EmailDto emailDto, String templateName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        var otpCode = this.generateOTP();
        try {
            Context context = new Context();
            context.setVariable("message", emailDto.getTo());
            context.setVariable("otpCode", otpCode);
            helper.setTo(emailDto.getTo());
            helper.setSubject("Xác thực tài khoản TOEICUTE");
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Gửi email thất bại");
        }
        return new ResponseVO(Boolean.TRUE, otpCode, null);
    }

    @Override
    public Object sendEmailAccount(LoginSocialDto loginSocialDto, String password, String templateName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        var otpCode = this.generateOTP();
        try {
            Context context = new Context();
            context.setVariable("email", loginSocialDto.getEmail());
            context.setVariable("password", password);
            context.setVariable("fullName", loginSocialDto.getFullName());
            context.setVariable("loginUrl", "http://localhost:4200/login");
            context.setVariable("loginText", "Toeicute");
            context.setVariable("provider", loginSocialDto.getProvider());
            helper.setTo(loginSocialDto.getEmail());
            helper.setSubject("THÔNG TIN TÀI KHOẢN TOEICUTE");
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Gửi email thất bại");
        }
        return new ResponseVO(Boolean.TRUE, otpCode, null);
    }
}
