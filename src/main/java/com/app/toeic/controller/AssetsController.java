package com.app.toeic.controller;


import com.app.toeic.repository.AssetsRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.FirebaseStorageService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final AssetsRepository assetsRepository;
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/assets/{path}")
    public ResponseVO getAssets(@PathVariable("path") String path) {
        return new ResponseVO(Boolean.TRUE, assetsRepository.findByPath(path).orElse(null), "Get assets successfully");
    }

    @GetMapping("/test")
    public ResponseVO test() {
        return new ResponseVO(Boolean.TRUE, "test", "Get assets successfully");
    }

    @PostMapping("/upload-file")
    public ResponseVO uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        var image = firebaseStorageService.uploadFile(file);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(image)
                .message("Upload hình ảnh thành công!")
                .build();
    }

}
