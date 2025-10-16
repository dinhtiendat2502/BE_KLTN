package com.app.toeic.firebase.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.firebase.model.FirebaseBean;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.HttpStatus;
import com.app.toeic.util.JsonConverter;
import com.app.toeic.util.URLHelper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Log
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    Storage storage;
    final FirebaseBean firebaseBean;
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";

    @PostConstruct
    public void init() throws IOException {
        if (org.apache.commons.lang3.StringUtils.isBlank(firebaseBean.getFirebaseConfig().getProjectId())) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE_CONFIG_NOT_FOUND");
        }
        var jsonContent = firebaseBean.getFirebaseConfig().getFileJson();
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> init >> {0}", jsonContent));
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
        storage = StorageOptions
                .newBuilder()
                .setCredentials(credentials)
                .setProjectId(firebaseBean.getFirebaseConfig().getProjectId())
                .build()
                .getService();

        var options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket(firebaseBean.getFirebaseConfig().getBucketName())
                .build();
        FirebaseApp.initializeApp(options);
    }


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        var firebaseConfig = firebaseBean.getFirebaseConfig();
        log.info(MessageFormat.format(
                "FirebaseStorageServiceImpl >> uploadFile >> firebaseConfig: {0}",
                JsonConverter.convertObjectToJson(firebaseConfig)
        ));
        var fileName = generateFileName(
                URLHelper.replaceByRegex(Objects.requireNonNull(file.getOriginalFilename())));
        var map = new HashMap<String, String>();
        map.put(firebaseConfig.getTokenKey(), fileName);
        var blobId = BlobId.of(firebaseConfig.getBucketName(), fileName);
        var blobInfo = BlobInfo
                .newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getInputStream());
        return String.format(
                DOWNLOAD_URL,
                firebaseConfig.getBucketName(),
                URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        );
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
