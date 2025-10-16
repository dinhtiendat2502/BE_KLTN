package com.app.toeic.komunicate.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.komunicate.model.KommunicateBot;
import com.app.toeic.komunicate.payload.KommunicateBotDTO;
import com.app.toeic.komunicate.repo.KommunicateBotRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kommunicate/bot")
@RequiredArgsConstructor
public class KommunicateBotController {
    private final KommunicateBotRepo kommunicateBotRepo;

    @GetMapping("/all")
    public Object getAll() {
        return kommunicateBotRepo.findAll();
    }

    @PostMapping("/update")
    public Object updateKommunicateBot(@RequestBody KommunicateBotDTO payload) {
        String[] msg = new String[1];
        kommunicateBotRepo
                .findByAppId(payload.getAppId())
                .ifPresentOrElse(kommunicateBot -> {
                    kommunicateBot.setApiKey(payload.getApiKey());
                    kommunicateBotRepo.save(kommunicateBot);
                    msg[0] = "UPDATE_KOMMUNICATE_BOT_SUCCESS";
                }, () -> {
                    kommunicateBotRepo
                            .save(KommunicateBot
                                    .builder()
                                    .appId(payload.getAppId())
                                    .apiKey(payload.getApiKey())
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
        kommunicateBotRepo
                .findById(id)
                .ifPresent(kommunicateBotRepo::delete);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_KOMMUNICATE_BOT_SUCCESS")
                .build();
    }
}
