package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


@UtilityClass
public class FileUtils {
    public FileInfo getInfoFromUrl(String link) throws IOException {
        var url = URI.create(link).toURL();
        var connection = url.openConnection();
        return new FileInfo(
                FilenameUtils.getName(url.getPath()),
                connection.getContentType(),
                connection.getContentLength(),
                connection.getInputStream()
        );
    }

    public record FileInfo(String fileName, String contentType, long fileSize, InputStream file) {}
}
