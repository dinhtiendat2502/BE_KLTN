package com.app.toeic.email.service.impl;


import com.app.toeic.email.repo.EmailTemplateRepo;
import com.app.toeic.email.service.EmailService;
import com.app.toeic.user.model.Otp;
import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.exception.AppException;
import com.app.toeic.user.repo.IOtpRepository;
import com.app.toeic.util.HttpStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    JavaMailSender mailSender;
    Random random = new Random();
    IOtpRepository otpRepository;
    EmailTemplateRepo emailTemplateRepo;

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
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
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
            case "AUTHENTICATION_AFTER_REGISTER" -> {
                var otp = this.generateOTP();
                var otpObj = Otp
                        .builder()
                        .otpCode(otp)
                        .action("REGISTER")
                        .email(emailTo)
                        .build();
                otpRepository.save(otpObj);
                yield templateContent.formatted(emailTo, otp);
            }
            case "FORGOT_PASSWORD" -> {
                var otp = emailTo;
                // hash otp
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashInBytes = md.digest(otp.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hashInBytes) {
                    sb.append(String.format("%02x", b));
                }
                otp = sb.toString();
                var otpObj = Otp
                        .builder()
                        .otpCode(otp)
                        .action("FORGOT_PASSWORD")
                        .email(emailTo)
                        .build();
                otpRepository.save(otpObj);
                yield templateContent.formatted("http://localhost:4200", STR."http://localhost:4200/reset-password/\{otp}");
            }
            case "LOGIN_SOCIAL" -> {
                String url = StringUtils.isEmpty(loginSocialDto.getUrl()) ? "http://localhost:4200" : loginSocialDto.getUrl();
                yield templateContent.formatted(
                        loginSocialDto.getEmail(),
                        loginSocialDto.getProvider(),
                        loginSocialDto.getEmail(),
                        loginSocialDto.getFullName(),
                        password,
                        url
                );
            }
            default -> templateContent;
        };
    }

    @Override
    public void sendEmailAccount(LoginSocialDTO loginSocialDto, String password, String templateName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
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
