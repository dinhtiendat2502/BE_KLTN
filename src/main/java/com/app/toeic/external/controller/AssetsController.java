package com.app.toeic.external.controller;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.service.FirebaseStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/test")
    public ResponseVO test(HttpServletRequest request) {
        return new ResponseVO(
                Boolean.TRUE,
                null,
                "Get assets successfully"
        );
    }

    @PostMapping(value = "/upload-file", consumes = {"multipart/form-data"})
    public ResponseVO uploadImage(@RequestParam("file") MultipartFile file) throws
            IOException {
        if (file == null) return new ResponseVO(
                Boolean.FALSE,
                "",
                "File is null"
        );

        var image = firebaseStorageService.uploadFile(file);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(image)
                .message("Upload hình ảnh thành công!")
                .build();
    }

    @PostMapping("/upload-files")
    public ResponseVO uploadImages(@RequestParam("files") MultipartFile[] files) throws
            IOException {
        var images = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            images[i] = firebaseStorageService.uploadFile(files[i]);
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(images)
                .message("Upload hình ảnh thành công!")
                .build();
    }

}
