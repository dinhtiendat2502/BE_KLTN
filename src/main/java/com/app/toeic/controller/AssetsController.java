package com.app.toeic.controller;


import com.app.toeic.repository.AssetsRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final AssetsRepository assetsRepository;

    @GetMapping("/assets/{path}")
    public ResponseVO getAssets(@PathVariable("path") String path) {
        return new ResponseVO(Boolean.TRUE, assetsRepository.findByPath(path).orElse(null), "Get assets successfully");
    }

    @GetMapping("/test")
    public ResponseVO test() {
        return new ResponseVO(Boolean.TRUE, "test", "Get assets successfully");
    }
}
