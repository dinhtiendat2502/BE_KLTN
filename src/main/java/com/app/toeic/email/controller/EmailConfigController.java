package com.app.toeic.email.controller;


import com.app.toeic.config.JavaMailConfig;
import com.app.toeic.email.model.EmailConfig;
import com.app.toeic.email.payload.EmailConfigDTO;
import com.app.toeic.email.repo.EmailConfigRepo;
import com.app.toeic.external.response.ResponseVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailConfigController {
    EmailConfigRepo emailConfigRepo;
    JavaMailConfig javaMailConfig;

    @GetMapping("/all")
    public Object getAll() {
        return emailConfigRepo.findAll();
    }

    @PostMapping("/update")
    public Object addEmailConfig(@RequestBody EmailConfigDTO payload) {
        String[] msg = new String[1];
        emailConfigRepo
                .findByUsername(payload.getUsername())
                .ifPresentOrElse(emailConfig -> {
                    emailConfig.setHost(payload.getHost());
                    emailConfig.setPort(payload.getPort());
                    emailConfig.setPassword(payload.getPassword());
                    emailConfigRepo.save(emailConfig);
                    msg[0] = "UPDATE_EMAIL_CONFIG_SUCCESS";
                }, () -> {
                    emailConfigRepo
                            .save(EmailConfig
                                          .builder()
                                          .host(payload.getHost())
                                          .port(payload.getPort())
                                          .username(payload.getUsername())
                                          .password(payload.getPassword())
                                          .build());
                    msg[0] = "ADD_EMAIL_CONFIG_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @PatchMapping("/update/status/{username}")
    public Object updateEmailConfigStatus(@PathVariable String username) {
        var emailConfig = emailConfigRepo.findByUsername(username).orElse(null);
        if (emailConfig == null) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("EMAIL_CONFIG_NOT_FOUND")
                    .build();
        }
        var list = emailConfigRepo.findAllByUsernameNot(username);
        list.forEach(item -> {
            item.setStatus(false);
            emailConfigRepo.save(item);
        });
        emailConfig.setStatus(true);
        emailConfigRepo.save(emailConfig);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_EMAIL_CONFIG_STATUS_SUCCESS")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteEmailConfig(@PathVariable Integer id) {
        emailConfigRepo
                .findById(id)
                .ifPresent(emailConfigRepo::delete);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_EMAIL_CONFIG_SUCCESS")
                .build();
    }

}
