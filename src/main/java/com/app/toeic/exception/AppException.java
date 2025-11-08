package com.app.toeic.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
    String code;
    String message;

    public static AppException error(String message) {
        return new AppException("400", message);
    }

    public AppException fail(String message) {
        return new AppException("500", message);
    }
}
