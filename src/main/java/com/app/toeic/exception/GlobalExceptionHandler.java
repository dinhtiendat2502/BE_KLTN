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
import java.text.MessageFormat;
import java.time.DateTimeException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException e) {
        log.error(MessageFormat.format("Exception >> GlobalExceptionHandler >> handleAppException: {0}", e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationException(MethodArgumentNotValidException e) {
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
    public Object handleFileSizeLimitExceededException(MaxUploadSizeExceededException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleFileSizeLimitExceededException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(MultipartException.class)
    public Object handleMultipartException(MultipartException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleMultipartException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleIOException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("READ_FILE_ERROR")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFoundException(NoHandlerFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleNotFoundException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("NOT_FOUND")
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Object handleBadCredentialsException(BadCredentialsException e) {
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
    public Object handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUsernameNotFoundException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Object handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
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
    public Object handleStorageException(StorageException e) {
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
    @ExceptionHandler(DateTimeException.class)
    public Object handleDateTimeException(DateTimeException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleDateTimeException: %s".formatted(e.getMessage()), e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("INVALID_DATE_FORMAT")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public Object handleUnwantedException(Exception e) {
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
