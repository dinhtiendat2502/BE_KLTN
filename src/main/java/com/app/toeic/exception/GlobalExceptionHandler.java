package com.app.toeic.exception;

import com.app.toeic.external.response.ResponseVO;
import com.google.cloud.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseVO handleAppException(AppException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleAppException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO handleValidationException(MethodArgumentNotValidException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleValidationException: %s".formatted(e.getMessage()), e);

        BindingResult bindingResult = e.getBindingResult();
        var errorMessages = bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "")
                .findFirst()
                .orElse("");

        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(errorMessages)
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseVO handleFileSizeLimitExceededException(MaxUploadSizeExceededException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleFileSizeLimitExceededException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseVO handleMultipartException(MultipartException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleMultipartException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseVO handleIOException(IOException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleIOException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("READ_FILE_ERROR")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO handleNotFoundException(NoHandlerFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleNotFoundException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("NOT_FOUND")
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseVO handleBadCredentialsException(BadCredentialsException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleBadCredentialsException: %s".formatted(e.getMessage()), e);
        var message = e.getMessage();
        if (message.contains("Bad credentials")) {
            message = "EMAIL_OR_PASSWORD_INCORRECT";
        }
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(message)
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseVO handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUsernameNotFoundException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseVO handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.error(
                "Exception >> GlobalExceptionHandler >> handleInternalAuthenticationServiceException: %s".formatted(e.getMessage()),
                e
        );
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseVO handleStorageException(StorageException e) {
        log.error(
                "Exception >> GlobalExceptionHandler >> StorageException: %s".formatted(e.getMessage()),
                e
        );
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO handleUnwantedException(Exception e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUnwantedException: {}", e
                .getClass()
                .getSimpleName());
        log.error("Exception >> GlobalExceptionHandler >> handleUnwantedException:", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SERVER_ERROR")
                .build();
    }
}
