package com.app.toeic.revai.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.revai.model.RevAIAccount;
import com.app.toeic.revai.payload.RevAIAccountDTO;
import com.app.toeic.revai.repo.RevAIAccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revai/account")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RevAIAccountController {
    RevAIAccountRepo revAIAccountRepo;

    @GetMapping("/all")
    public Object getAll() {
        return revAIAccountRepo.findAll();
    }

    @PostMapping("/update")
    public Object updateRevAIAccount(@RequestBody RevAIAccountDTO payload) {
        final String[] msg = new String[1];
        revAIAccountRepo
                .findByEmail(payload.getEmail())
                .ifPresentOrElse(revAIAccount -> {
                    revAIAccount.setPassword(payload.getPassword());
                    revAIAccountRepo.save(revAIAccount);
                    msg[0] = "UPDATE_REV_AI_ACCOUNT_SUCCESS";
                }, () -> {
                    revAIAccountRepo
                            .save(RevAIAccount
                                    .builder()
                                    .email(payload.getEmail())
                                    .password(payload.getPassword())
                                    .build());
                    msg[0] = "ADD_REV_AI_ACCOUNT_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") Integer id) {
        RevAIAccount revAIAccount = revAIAccountRepo.findById(id).orElse(null);
        if (revAIAccount == null) {
            return ResponseVO.builder().success(false).message("ACCOUNT_NOT_FOUND").data(null).build();
        }
        revAIAccountRepo.delete(revAIAccount);
        return ResponseVO.builder().success(true).message("DELETE_ACCOUNT_SUCCESS").data(null).build();
    }

}
