package com.app.toeic.email.controller;

import com.app.toeic.email.model.EmailTemplate;
import com.app.toeic.email.payload.EmailTemplateDTO;
import com.app.toeic.email.repo.EmailTemplateRepo;
import com.app.toeic.external.response.ResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email/template")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class EmailTemplateController {
    EmailTemplateRepo emailTemplateRepo;

    @GetMapping("/all")
    public Object getAll() {
        return emailTemplateRepo.findAll();
    }

    @PostMapping("/update")
    public Object updateEmailTemplate(@RequestBody EmailTemplateDTO payload) {
        String[] msg = new String[1];
        emailTemplateRepo
                .findByTemplateCode(payload.getTemplateCode())
                .ifPresentOrElse(emailTemplate -> {
                    emailTemplate.setName(payload.getName());
                    emailTemplate.setSubject(payload.getSubject());
                    emailTemplate.setTemplateContent(payload.getTemplateContent());
                    emailTemplateRepo.save(emailTemplate);
                    msg[0] = "UPDATE_EMAIL_TEMPLATE_SUCCESS";
                }, () -> {
                    emailTemplateRepo
                            .save(EmailTemplate
                                    .builder()
                                    .name(payload.getName())
                                    .subject(payload.getSubject())
                                    .templateContent(payload.getTemplateContent())
                                    .templateCode(payload.getTemplateCode())
                                    .build());
                    msg[0] = "ADD_EMAIL_TEMPLATE_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteEmailTemplate(@PathVariable Integer id) {
        emailTemplateRepo
                .findById(id)
                .ifPresent(emailTemplateRepo::delete);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_EMAIL_TEMPLATE_SUCCESS")
                .build();
    }
}
