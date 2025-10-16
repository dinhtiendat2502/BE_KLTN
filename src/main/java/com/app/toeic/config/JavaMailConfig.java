package com.app.toeic.config;

import com.app.toeic.email.model.EmailConfig;
import com.app.toeic.email.repo.EmailConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class JavaMailConfig {
    EmailConfigRepo emailConfigRepo;

    @Bean
    public JavaMailSender javaMailSender() {
        var emailConfig = emailConfigRepo.findByStatus(true).orElse(new EmailConfig());
        var mailSender = new JavaMailSenderImpl();
        var mailProperties = buildJavaMailProperties();
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setProtocol("smtp");
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());
        mailSender.setDefaultEncoding("UTF-8");
        return mailSender;
    }

    private static Properties buildJavaMailProperties() {
        var mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        mailProperties.put("mail.smtp.starttls.required", true);
        mailProperties.put("mail.smtp.socketFactory.port", 465);
        mailProperties.put("mail.smtp.debug", true);
        mailProperties.put("mail.smtp.ssl.checkserveridentity", true);
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.fallback", false);
        return mailProperties;
    }
}
