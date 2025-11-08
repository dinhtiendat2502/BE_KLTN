package com.app.toeic.exception;

import com.app.toeic.external.response.ResponseVO;
import com.google.cloud.storage.StorageException;
import io.undertow.server.RequestTooBigException;
import lombok.extern.java.Log;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.DateTimeException;
import java.util.logging.Level;

@Log
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException e) {
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationException(MethodArgumentNotValidException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleValidationException: {0}", e);

        var bindingResult = e.getBindingResult();
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

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFoundException(NoResourceFoundException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleNoResourceFoundException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("API_NOT_FOUND")
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleFileSizeLimitExceededException(MaxUploadSizeExceededException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleFileSizeLimitExceededException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(RequestTooBigException.class)
    public Object handleFileSizeLimitExceededException(RequestTooBigException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> RequestTooBigException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(MultipartException.class)
    public Object handleMultipartException(MultipartException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleMultipartException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SIZE_LIMIT_EXCEEDED")
                .build();
    }

    @ExceptionHandler(IOException.class)
    public Object handleIOException(IOException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleIOException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("READ_FILE_ERROR")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFoundException(NoHandlerFoundException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleNotFoundException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("NOT_FOUND")
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Object handleBadCredentialsException(BadCredentialsException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleBadCredentialsException: {0}", e);
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
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleUsernameNotFoundException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Object handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        log.log(
                Level.WARNING,
                "Exception >> GlobalExceptionHandler >> handleInternalAuthenticationServiceException: {0}",
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
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> StorageException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DateTimeException.class)
    public Object handleDateTimeException(DateTimeException e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleDateTimeException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("INVALID_DATE_FORMAT")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public Object handleUnwantedException(Exception e) {
        log.log(Level.WARNING, "Exception >> GlobalExceptionHandler >> handleUnwantedException: {0}", e);
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .message("SERVER_ERROR")
                .build();
    }
}
