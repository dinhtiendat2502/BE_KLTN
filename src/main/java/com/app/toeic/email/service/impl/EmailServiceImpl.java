package com.app.toeic.email.service.impl;


import com.app.toeic.config.JavaMailConfig;
import com.app.toeic.email.repo.EmailTemplateRepo;
import com.app.toeic.email.service.EmailService;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.user.model.Otp;
import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.exception.AppException;
import com.app.toeic.user.repo.IOtpRepository;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    JavaMailConfig javaMailConfig;
    Random random = new Random();
    IOtpRepository otpRepository;
    EmailTemplateRepo emailTemplateRepo;
    SystemConfigService systemConfigService;

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
    public void sendEmail(String emailTo, String templateCode) {
        var mailSender = javaMailConfig.buildJavaMailSender();
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            var emailTemplate = emailTemplateRepo
                    .findByTemplateCode(templateCode)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "EMAIL_TEMPLATE_NOT_FOUND"));
            var templateContent = getTemplateHtml(
                    emailTo,
                    templateCode,
                    emailTemplate.getTemplateContent(),
                    null,
                    null
            );
            helper.setTo(emailTo);
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(templateContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | NoSuchAlgorithmException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "SEND_EMAIL_FAILED");
        }
    }

    private String getTemplateHtml(
            String emailTo,
            String templateCode,
            String templateContent,
            String password,
            LoginSocialDTO loginSocialDto
    ) throws NoSuchAlgorithmException {
        return switch (templateCode) {
            case Constant.AUTHENTICATION_AFTER_REGISTER -> {
                var otp = this.generateOTP();

                var otpObj = otpRepository
                        .findByEmailAndAction(emailTo, Constant.REGISTER)
                        .orElse(Otp
                                .builder()
                                .action(Constant.REGISTER)
                                .email(emailTo)
                                .build());
                otpObj.setOtpCode(otp);
                otpObj.setCreatedAt(LocalDateTime.now());
                otpRepository.save(otpObj);
                yield templateContent.formatted(emailTo, otp);
            }
            case Constant.FORGOT_PASSWORD -> {
                var otp = emailTo;
                // hash otp
                var md = MessageDigest.getInstance("SHA-256");
                var hashInBytes = md.digest(otp.getBytes(StandardCharsets.UTF_8));
                var sb = new StringBuilder();
                for (byte b : hashInBytes) {
                    sb.append(String.format("%02x", b));
                }
                otp = sb.toString();
                var otpObj = otpRepository
                        .findByEmailAndAction(emailTo, Constant.FORGOT_PASSWORD)
                        .orElse(Otp
                                .builder()
                                .action(Constant.FORGOT_PASSWORD)
                                .email(emailTo)
                                .build());
                otpObj.setOtpCode(otp);
                otpObj.setCreatedAt(LocalDateTime.now());
                otpRepository.save(otpObj);
                var urlFE = systemConfigService.getConfigValue(Constant.URL_FRONTEND);
                yield templateContent.formatted(
                        urlFE,
                        STR."\{urlFE}/reset-password/\{otp}"
                );
            }
            case Constant.LOGIN_SOCIAL -> templateContent.formatted(
                    loginSocialDto.getEmail(),
                    loginSocialDto.getProvider(),
                    loginSocialDto.getEmail(),
                    loginSocialDto.getFullName(),
                    password,
                    loginSocialDto.getUrl()
            );
            default -> templateContent;
        };
    }

    @Override
    public void sendEmailAccount(LoginSocialDTO loginSocialDto, String password, String templateName) {
        var mailSender = javaMailConfig.buildJavaMailSender();
        var mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            var emailTemplate = emailTemplateRepo
                    .findByTemplateCode(templateName)
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "EMAIL_TEMPLATE_NOT_FOUND"));
            var templateContent = getTemplateHtml(
                    null,
                    templateName,
                    emailTemplate.getTemplateContent(),
                    password,
                    loginSocialDto
            );
            helper.setTo(loginSocialDto.getEmail());
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(templateContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | NoSuchAlgorithmException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "SEND_EMAIL_FAILED");
        }
    }
}
