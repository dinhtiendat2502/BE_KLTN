package com.app.toeic.komunicate.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.komunicate.model.KommunicateAccount;
import com.app.toeic.komunicate.payload.KommunicateAccountDTO;
import com.app.toeic.komunicate.repo.KommunicateAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kommunicate/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class KommunicateAccountController {
    private final KommunicateAccountRepo kommunicateAccountRepo;

    @GetMapping("/all")
    public Object getAll() {
        return kommunicateAccountRepo.findAll();
    }

    @PostMapping("/update")
    public Object updateKommunicateAccount(@RequestBody KommunicateAccountDTO payload) {
        String[] msg = new String[1];
        kommunicateAccountRepo
                .findByEmail(payload.getEmail())
                .ifPresentOrElse(kommunicateAccount -> {
                    kommunicateAccount.setPassword(payload.getPassword());
                    kommunicateAccountRepo.save(kommunicateAccount);
                    msg[0] = "UPDATE_KOMMUNICATE_ACCOUNT_SUCCESS";
                }, () -> {
                    kommunicateAccountRepo
                            .save(KommunicateAccount
                                    .builder()
                                    .email(payload.getEmail())
                                    .password(payload.getPassword())
                                    .build());
                    msg[0] = "ADD_KOMMUNICATE_ACCOUNT_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteKommunicateAccount(@PathVariable Integer id) {
        kommunicateAccountRepo.deleteById(id);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_KOMMUNICATE_ACCOUNT_SUCCESS")
                .build();
    }
}
