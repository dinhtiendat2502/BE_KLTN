package com.app.toeic.firebase.service.impl;

import com.app.toeic.cache.FirebaseConfigCache;
import com.app.toeic.exception.AppException;
import com.app.toeic.firebase.model.FirebaseConfig;
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
import java.util.Map;
import java.util.UUID;

@Log
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    final FirebaseConfigCache firebaseConfigCache;
    final FirebaseUploadHistoryRepo firebaseUploadHistoryRepo;
    static String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/{0}/o/%s?alt=media";
    static String gsUrl = "gs://{0}/%s";

    private FirebaseConfig previousFirebaseBean;

    @PostConstruct
    public synchronized void init() throws IOException {
        updateFirebaseConfig();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, false).get("url");
    }

    @Override
    public Map<String, String> uploadFile(MultipartFile file, boolean isGcs) throws IOException {
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
        return Map.of("url", url, "gcsUrl", getGcsUrl(name));
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

    @Override
    public void updateFirebaseConfig() throws IOException {
        var firebaseBean = firebaseConfigCache.getConfigValue(true);
        if (!firebaseBean.equals(previousFirebaseBean)) { // Kiểm tra xem có thay đổi không
            if (org.apache.commons.lang3.StringUtils.isBlank(firebaseBean.getProjectId())) {
                throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE_CONFIG_NOT_FOUND");
            }
            downloadUrl = MessageFormat.format(downloadUrl, firebaseBean.getBucketName());
            gsUrl = MessageFormat.format(gsUrl, firebaseBean.getBucketName());
            var jsonContent = firebaseBean.getFileJson();
            var credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent.getBytes()));
            var options = FirebaseOptions.builder()
                                         .setCredentials(credentials)
                                         .setStorageBucket(firebaseBean.getBucketName())
                                         .build();
            FirebaseApp.initializeApp(options);
            previousFirebaseBean = firebaseBean; // Cập nhật tham chiếu đến phiên bản mới
        }
    }

    @Override
    public Object getAllFiles() {
        var bucket = StorageClient.getInstance().bucket();
        var rs = bucket.list()
                       .streamAll()
                       .map(blob -> {
                           var url = getImageUrl(blob.getName());
                           var contentType = blob.getContentType();
                           var fileSize = blob.getSize();
                           return Map.of("url", url, "contentType", contentType, "fileSize", fileSize);
                       })
                       .toList();
        log.info(MessageFormat.format("FirebaseStorageServiceImpl >> getAllFiles >> {0}", rs.size()));
        return rs;
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
