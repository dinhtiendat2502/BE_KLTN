package com.app.toeic.translate.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.translate.service.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TranslateController {
    TranslateService translateService;

    @PostMapping("translate")
    public Object translate(@RequestBody String content) {
        return ResponseVO
                .builder()
                .success(true)
                .data(translateService.translate(content))
                .build();
    }
}
