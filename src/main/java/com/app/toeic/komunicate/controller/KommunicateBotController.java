package com.app.toeic.komunicate.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.komunicate.model.KommunicateBot;
import com.app.toeic.komunicate.payload.KommunicateBotDTO;
import com.app.toeic.komunicate.repo.KummunicateBotRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kommunicate/bot")
@CrossOrigin("*")
@RequiredArgsConstructor
public class KommunicateBotController {
    private final KummunicateBotRepo kummunicateBotRepo;

    @GetMapping("/all")
    public Object getAll() {
        return kummunicateBotRepo.findAll();
    }

    @PostMapping("/update")
    public Object updateKommunicateBot(@RequestBody KommunicateBotDTO payload) {
        String[] msg = new String[1];
        kummunicateBotRepo
                .findByAppId(payload.getAppId())
                .ifPresentOrElse(kommunicateBot -> {
                    kommunicateBot.setApiKey(payload.getApiKey());
                    kummunicateBotRepo.save(kommunicateBot);
                    msg[0] = "UPDATE_KOMMUNICATE_BOT_SUCCESS";
                }, () -> {
                    kummunicateBotRepo
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
        kummunicateBotRepo.updateAllByAppIdNot(appId, "INACTIVE");
        kummunicateBotRepo
                .findByAppId(appId)
                .ifPresent(kommunicateBot -> {
                    kommunicateBot.setStatus("ACTIVE");
                    kummunicateBotRepo.save(kommunicateBot);
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_KOMMUNICATE_BOT_STATUS_SUCCESS")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public Object deleteKommunicateBot(@PathVariable Integer id) {
        kummunicateBotRepo
                .findById(id)
                .ifPresent(kummunicateBotRepo::delete);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_KOMMUNICATE_BOT_SUCCESS")
                .build();
    }
}
