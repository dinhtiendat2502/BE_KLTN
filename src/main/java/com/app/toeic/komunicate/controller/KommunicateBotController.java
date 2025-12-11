package com.app.toeic.komunicate.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.komunicate.model.KommunicateBot;
import com.app.toeic.komunicate.payload.KommunicateBotDTO;
import com.app.toeic.komunicate.repo.KommunicateBotRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kommunicate/bot")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KommunicateBotController {
    KommunicateBotRepo kommunicateBotRepo;

    @GetMapping("/all")
    public Object getAll() {
        return kommunicateBotRepo.findAll();
    }

    @GetMapping("get-bot-active")
    public Object getBotActive() {
        return ResponseVO
                .builder()
                .success(true)
                .data(kommunicateBotRepo.findAllByStatus(true)
                                        .getFirst())
                .build();
    }

    @PostMapping("/update")
    public Object updateKommunicateBot(@RequestBody KommunicateBotDTO payload) {
        final var msg = new String[1];
        kommunicateBotRepo
                .findByAppId(payload.getAppId())
                .ifPresentOrElse(kommunicateBot -> {
                    kommunicateBot.setApiKey(payload.getApiKey());
                    kommunicateBot.setScript(payload.getScript());
                    kommunicateBotRepo.save(kommunicateBot);
                    msg[0] = "UPDATE_KOMMUNICATE_BOT_SUCCESS";
                }, () -> {
                    kommunicateBotRepo
                            .save(KommunicateBot
                                          .builder()
                                          .appId(payload.getAppId())
                                          .apiKey(payload.getApiKey())
                                          .script(payload.getScript())
                                          .build());
                    msg[0] = "ADD_KOMMUNICATE_BOT_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @PatchMapping("/update/status/{appId}")
    public Object updateKommunicateBotStatus(@PathVariable String appId) {
        var kommunicateBot = kommunicateBotRepo.findByAppId(appId).orElse(null);
        if (kommunicateBot == null) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("KOMMUNICATE_BOT_NOT_FOUND")
                    .build();
        }
        var list = kommunicateBotRepo.findAllByAppIdNot(appId);
        list.forEach(item -> {
            item.setStatus(false);
            kommunicateBotRepo.save(item);
        });
        kommunicateBot.setStatus(true);
        kommunicateBotRepo.save(kommunicateBot);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_KOMMUNICATE_BOT_STATUS_SUCCESS")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteKommunicateBot(@PathVariable Integer id) {
        kommunicateBotRepo.deleteById(id);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_KOMMUNICATE_BOT_SUCCESS")
                .build();
    }
}
