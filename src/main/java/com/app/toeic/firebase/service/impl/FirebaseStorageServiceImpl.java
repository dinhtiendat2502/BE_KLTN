package com.app.toeic.firebase.service.impl;

import com.app.toeic.cache.FirebaseConfigCache;
import com.app.toeic.exception.AppException;
import com.app.toeic.firebase.model.FirebaseUploadHistory;
import com.app.toeic.firebase.repo.FirebaseUploadHistoryRepo;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.util.FileUtils;
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
    static String gsUrl = "gs://{0}/%s";

    @PostConstruct
    public synchronized void init() throws IOException {
        var firebaseBean = firebaseConfigCache.getConfigValue(true);
        if (org.apache.commons.lang3.StringUtils.isBlank(firebaseBean.getProjectId())) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE_CONFIG_NOT_FOUND");
        }
        downloadUrl = MessageFormat.format(downloadUrl, firebaseBean.getBucketName());
        gsUrl = MessageFormat.format(gsUrl, firebaseBean.getBucketName());
        var jsonContent = firebaseBean.getFileJson();
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> init >> {0}", jsonContent));
        var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
        var options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket(firebaseBean.getBucketName())
                .build();
        FirebaseApp.initializeApp(options);
    }


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, false);
    }

    @Override
    public String uploadFile(MultipartFile file, boolean isGcs) throws IOException {
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
        return isGcs ? getGcsUrl(name) : url;
    }

    @Override
    public String uploadFile(FileUtils.FileInfo file) {
        var bucket = StorageClient.getInstance().bucket();
        bucket.create(file.fileName(), file.file(), file.contentType());
        var url = getImageUrl(file.fileName());
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> uploadFile >> {0}", url));
        // save info to database
        var history = FirebaseUploadHistory
                .builder()
                .fileName(file.fileName())
                .fileUrl(url)
                .fileType(file.contentType())
                .fileSize(file.fileSize())
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

    private String getImageUrl(String name) {
        return String.format(downloadUrl, name);
    }

    private String getGcsUrl(String name) {
        return String.format(gsUrl, name);
    }
}
