package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        InputStream fileStream;

        if (contentLength == -1) {
            connection.disconnect();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            fileStream = connection.getInputStream();
            var buffer = new ByteArrayOutputStream();
            var data = new byte[16384];
            int nRead;
            while ((nRead = fileStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            var fileContent = buffer.toByteArray();
            contentLength = fileContent.length;
            fileStream = new ByteArrayInputStream(fileContent);
        } else {
            fileStream = connection.getInputStream();
        }
        var imageBytes = IOUtils.toByteArray(url.openStream());
        return new FileInfo(fileName, contentType, contentLength, fileStream, imageBytes);
    }

    public boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image");
    }

    public boolean isAudio(String contentType) {
        return contentType != null && contentType.startsWith("audio");
    }

    public boolean isNotEmpty(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    public record FileInfo(String fileName, String contentType, long fileSize, InputStream file, byte[] imageBytes) {
    }
}
