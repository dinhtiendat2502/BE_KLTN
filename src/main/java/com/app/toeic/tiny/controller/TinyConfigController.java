package com.app.toeic.tiny.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.tiny.model.TinyConfig;
import com.app.toeic.tiny.repo.TinyConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tiny-config")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TinyConfigController {
    TinyConfigRepo tinyConfigRepo;

    @GetMapping("list")
    public Object getAllTinyConfig(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return tinyConfigRepo.findAll(PageRequest.of(page, size));
    }

    @PostMapping("add")
    public Object addTinyConfig(@RequestParam("apiKey") String apiKey) {
        var newTinyConfig = TinyConfig
                .builder()
                .apiKey(apiKey)
                .build();
        tinyConfigRepo.save(newTinyConfig);
        return ResponseVO
                .builder()
                .success(true)
                .message("ADD_TINY_CONFIG_SUCCESS")
                .build();
    }

    @PostMapping("update/{id}")
    public Object updateTinyConfig(@PathVariable("id") Long id, @RequestParam("apiKey") String apiKey) {
        var tinyConfig = tinyConfigRepo.findById(id);
        tinyConfig.ifPresent(tiny -> {
            tiny.setApiKey(apiKey);
            tinyConfigRepo.save(tiny);
        });
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_TINY_CONFIG_SUCCESS")
                .build();
    }

    @PatchMapping("update/status/{id}")
    public Object updateTinyConfigStatus(@PathVariable("id") Long id) {
        var listTinyConfig = tinyConfigRepo.findAllByTinyConfigIdNot(id);
        listTinyConfig.forEach(tiny -> {
            tiny.setStatus(false);
            tinyConfigRepo.save(tiny);
        });

        var tinyConfig = tinyConfigRepo.findById(id);
        tinyConfig.ifPresent(tiny -> {
            tiny.setStatus(true);
            tinyConfigRepo.save(tiny);
        });
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_TINY_CONFIG_STATUS_SUCCESS")
                .build();
    }

    @DeleteMapping("delete/{id}")
    public Object deleteTinyConfig(@PathVariable("id") Long id) {
        tinyConfigRepo.deleteById(id);
        return ResponseVO
                .builder()
                .success(true)
                .message("DELETE_TINY_CONFIG_SUCCESS")
                .build();
    }
}
