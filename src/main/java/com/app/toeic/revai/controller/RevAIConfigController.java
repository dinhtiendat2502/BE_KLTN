package com.app.toeic.revai.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.revai.model.RevAIConfig;
import com.app.toeic.revai.repo.RevAIConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revai/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RevAIConfigController {
    RevAIConfigRepo revAIConfigRepo;
    private static final String CONFIG_NOT_FOUND = "CONFIG_NOT_FOUND";
    @GetMapping("/all")
    public Object getAll() {
        return revAIConfigRepo.findAll();
    }

    @PostMapping("/add")
    public Object add(@RequestParam("accessToken") String accessToken) {
        revAIConfigRepo.save(RevAIConfig.builder().accessToken(accessToken).build());
        return ResponseVO.builder().success(true).message("ADD_CONFIG_REV_AI_SUCCESS").data(null).build();
    }

    @PatchMapping("/update/status/{id}")
    public Object updateStatus(@PathVariable("id") Integer id) {
        RevAIConfig revAIConfig = revAIConfigRepo.findById(id).orElse(null);
        if (revAIConfig == null) {
            return ResponseVO.builder().success(false).message(CONFIG_NOT_FOUND).data(null).build();
        }
        revAIConfig.setStatus(true);
        revAIConfigRepo.save(revAIConfig);
        var listRevAIConfig = revAIConfigRepo.findAllByAccessTokenNot(revAIConfig.getAccessToken());
        listRevAIConfig.forEach(config -> {
            config.setStatus(false);
            revAIConfigRepo.save(config);
        });
        return ResponseVO.builder().success(true).message("UPDATE_STATUS_REV_AI_SUCCESS").data(null).build();
    }

    @PatchMapping("/update/{id}")
    public Object update(@PathVariable("id") Integer id, @RequestParam("accessToken") String accessToken) {
        RevAIConfig revAIConfig = revAIConfigRepo.findById(id).orElse(null);
        if (revAIConfig == null) {
            return ResponseVO.builder().success(false).message(CONFIG_NOT_FOUND).data(null).build();
        }
        revAIConfig.setAccessToken(accessToken);
        revAIConfigRepo.save(revAIConfig);
        return ResponseVO.builder().success(true).message("UPDATE_CONFIG_REV_AI_SUCCESS").data(null).build();
    }

    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") Integer id) {
        RevAIConfig revAIConfig = revAIConfigRepo.findById(id).orElse(null);
        if (revAIConfig == null) {
            return ResponseVO.builder().success(false).message(CONFIG_NOT_FOUND).data(null).build();
        }
        revAIConfigRepo.delete(revAIConfig);
        return ResponseVO.builder().success(true).message("DELETE_CONFIG_REV_AI_SUCCESS").data(null).build();
    }

}
