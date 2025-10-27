package com.app.toeic.firebase.service;

import com.app.toeic.util.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface FirebaseStorageService {
    String uploadFile(MultipartFile file) throws IOException;
    String uploadFile(MultipartFile file, boolean isGcs) throws IOException;
    String uploadFile(FileUtils.FileInfo file);
    void delete(String name) throws IOException;


}
