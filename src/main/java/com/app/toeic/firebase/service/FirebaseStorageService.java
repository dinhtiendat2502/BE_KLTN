package com.app.toeic.firebase.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseStorageService {
    String uploadFile(MultipartFile file) throws IOException;
    String uploadFile(MultipartFile file, boolean isGcs) throws IOException;
    void delete(String name) throws IOException;


}
