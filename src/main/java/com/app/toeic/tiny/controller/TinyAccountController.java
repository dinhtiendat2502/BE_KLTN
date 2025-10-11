package com.app.toeic.tiny.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.tiny.model.TinyAccount;
import com.app.toeic.tiny.payload.TinyAccountPayload;
import com.app.toeic.tiny.repo.TinyAccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("tiny-account")
public class TinyAccountController {
    TinyAccountRepo tinyAccountRepo;

    @GetMapping("list")
    public Object getAllTinyAccount(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return tinyAccountRepo.findAll(PageRequest.of(page, size));
    }

    @PostMapping("update")
    public Object updateTinyAccount(@RequestBody TinyAccountPayload payload) {
        var tinyAccount = tinyAccountRepo.findByUsername(payload.getUsername());
        final String[] msg = new String[1];
        tinyAccount.ifPresentOrElse(tiny -> {
            tiny.setPassword(payload.getPassword());
            tinyAccountRepo.save(tiny);
            msg[0] = "UPDATE_TINY_ACCOUNT_SUCCESS";
        }, () -> {
            var newTinyAccount = TinyAccount
                    .builder()
                    .username(payload.getUsername())
                    .password(payload.getPassword())
                    .build();
            tinyAccountRepo.save(newTinyAccount);
            msg[0] = "CREATE_TINY_ACCOUNT_SUCCESS";
        });
        return ResponseVO
                .builder()
                .success(true)
                .message(msg[0])
                .build();
    }

    @DeleteMapping("delete/{id}")
    public Object deleteTinyAccount(@PathVariable("id") Long id) {
        tinyAccountRepo.deleteById(id);
        return ResponseVO
                .builder()
                .success(true)
                .message("DELETE_TINY_ACCOUNT_SUCCESS")
                .build();
    }
}
