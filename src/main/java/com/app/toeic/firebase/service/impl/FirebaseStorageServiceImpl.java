package com.app.toeic.firebase.service.impl;

import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.URLHelper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    private final Storage storage;

    @Value("${firebase.project-id}")
    private String projectID;

    @Value("${firebase.token-key}")
    private String tokenUpload;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    private static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";


    public FirebaseStorageServiceImpl() throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
        storage = StorageOptions
                .newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .setProjectId(projectID)
                .build()
                .getService();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(
                URLHelper.replaceByRegex(Objects.requireNonNull(file.getOriginalFilename())));
        Map<String, String> map = new HashMap<>();
        map.put(tokenUpload, fileName);
        var blobId = BlobId.of(bucketName, fileName);
        var blobInfo = BlobInfo
                .newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getInputStream());
        return String.format(DOWNLOAD_URL, bucketName, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
