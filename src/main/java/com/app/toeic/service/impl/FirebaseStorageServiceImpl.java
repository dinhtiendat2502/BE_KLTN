package com.app.toeic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.service.FirebaseStorageService;
import com.app.toeic.util.HttpStatus;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    private Storage storage;

    @Value("${firebase.project-id}")
    private String PROJECT_ID;

    @Value("${firebase.token-key}")
    private String TOKEN_UPLOAD;

    @Value("${firebase.bucket-name}")
    private String BUCKET_NAME;

    private final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";


    public FirebaseStorageServiceImpl() throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
        storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).setProjectId(PROJECT_ID).build().getService();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());
        Map<String, String> map = new HashMap<>();
        map.put(TOKEN_UPLOAD, fileName);
        var blobId = BlobId.of(BUCKET_NAME, fileName);
        var blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getInputStream());
        return String.format(DOWNLOAD_URL, BUCKET_NAME,URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }
}
