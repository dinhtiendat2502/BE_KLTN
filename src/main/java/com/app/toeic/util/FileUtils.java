package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;


@UtilityClass
public class FileUtils {
    public FileInfo getInfoFromUrl(String link) throws IOException {
        var url = URI.create(link).toURL();
        var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");

        var fileName = FilenameUtils.getName(url.getPath());
        var contentType = connection.getContentType();
        var contentLength = connection.getContentLengthLong();
        InputStream fileStream = connection.getInputStream();
        if (contentLength == -1) {
            var size = connection.getHeaderField("Cf-Polished").split("=")[1];
            contentLength = Long.parseLong(size);
        }
        var imageBytes = IOUtils.toByteArray(url.openStream());
        return new FileInfo(fileName, contentType, contentLength, fileStream, imageBytes);
    }

    public boolean isImage(MultipartFile file) {
        return checkFileType(file, "image");
    }

    public boolean isNotImage(MultipartFile file) {
        return !isImage(file);
    }

    public boolean isAudio(MultipartFile file) {
        return checkFileType(file, "audio");
    }

    public boolean isNotAudio(MultipartFile file) {
        return !isAudio(file);
    }

    private boolean checkFileType(MultipartFile file, String type) {
        return file != null && StringUtils.isNotBlank(file.getContentType()) && file.getContentType().startsWith(type);
    }

    public record FileInfo(String fileName, String contentType, long fileSize, InputStream file, byte[] imageBytes) {
    }
}
