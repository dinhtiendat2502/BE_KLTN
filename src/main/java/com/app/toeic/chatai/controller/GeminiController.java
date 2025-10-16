package com.app.toeic.chatai.controller;

import com.app.toeic.external.response.ResponseVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Log
@RestController
@RequestMapping("gemini")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeminiController {

    @PostMapping("ask/v1")
    public Object askGeminiV1(@RequestBody GeminiPayload payload) {

        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    public record GeminiPayload(List<String> listMessage, String newMessage) {
    }
}
