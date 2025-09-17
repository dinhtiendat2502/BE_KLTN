package com.app.toeic.controller;


import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final FirebaseStorageService firebaseStorageService;

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
