package com.app.toeic.controller;


import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.FirebaseStorageService;
import com.app.toeic.util.ServerHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class AssetsController {
    private final FirebaseStorageService firebaseStorageService;
    @Value("${ipinfo.token}")
    private String IP_TOKEN;

    @GetMapping("/test")
    public ResponseVO test(HttpServletRequest request) {
        var ipInfo = new IPinfo.Builder().setToken(IP_TOKEN).build();
        var ipAddress = ServerHelper.getClientIpAddr(request);
        System.out.println(ipAddress);
        try {
            var response = ipInfo.lookupIP(ipAddress);
            return new ResponseVO(Boolean.TRUE, response, "Get assets successfully");
        } catch (RateLimitedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload-file")
    public ResponseVO uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) return new ResponseVO(Boolean.FALSE, "", "File is null");

        var image = firebaseStorageService.uploadFile(file);
        return ResponseVO.builder().success(Boolean.TRUE).data(image).message("Upload hình ảnh thành công!").build();
    }

    @PostMapping("/upload-files")
    public ResponseVO uploadImages(@RequestParam("files") MultipartFile[] files) throws IOException {
        var images = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            images[i] = firebaseStorageService.uploadFile(files[i]);
        }
        return ResponseVO.builder().success(Boolean.TRUE).data(images).message("Upload hình ảnh thành công!").build();
    }

}
