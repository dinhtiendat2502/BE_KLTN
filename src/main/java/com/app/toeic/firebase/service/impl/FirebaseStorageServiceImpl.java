package com.app.toeic.firebase.service.impl;

import com.app.toeic.cache.FirebaseConfigCache;
import com.app.toeic.exception.AppException;
import com.app.toeic.firebase.model.FirebaseUploadHistory;
import com.app.toeic.firebase.repo.FirebaseUploadHistoryRepo;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.HttpStatus;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
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
import java.text.MessageFormat;
import java.util.UUID;

@Log
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    FirebaseConfigCache firebaseConfigCache;
    FirebaseUploadHistoryRepo firebaseUploadHistoryRepo;
    static String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/{0}/o/%s?alt=media";

    @PostConstruct
    public void init() throws IOException {
        var firebaseBean = firebaseConfigCache.getConfigValue(true);
        if (org.apache.commons.lang3.StringUtils.isBlank(firebaseBean.getProjectId())) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE_CONFIG_NOT_FOUND");
        }
        downloadUrl = MessageFormat.format(downloadUrl, firebaseBean.getBucketName());
        var jsonContent = firebaseBean.getFileJson();
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> init >> {0}", jsonContent));
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
        var options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket(firebaseBean.getBucketName())
                .build();
        FirebaseApp.initializeApp(options);
    }


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        var bucket = StorageClient.getInstance().bucket();
        var name = generateFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());
        var url = getImageUrl(name);
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> uploadFile >> {0}", url));

        // save info to database
        var history = FirebaseUploadHistory
                .builder()
                .fileName(name)
                .fileUrl(url)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
        firebaseUploadHistoryRepo.save(history);
        return url;
    }

    @Override
    public void delete(String name) throws IOException {
        var bucket = StorageClient.getInstance().bucket();
        if (org.apache.commons.lang3.StringUtils.isEmpty(name)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "FILE_NAME_IS_EMPTY");
        }
        var blob = bucket.get(name);
        if (blob == null) {
            throw new AppException(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND");
        }
        blob.delete();
    }


    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    public String getImageUrl(String name) {
        return String.format(downloadUrl, name);
    }
}
