package com.app.toeic.exception;

import com.app.toeic.response.ResponseVO;
import com.app.toeic.util.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseVO handleAppException(AppException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleAppException: {}", e.getMessage());
        return new ResponseVO(Boolean.FALSE, "", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO handleValidationException(MethodArgumentNotValidException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleValidationException: {}", e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        var errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "")
                .findFirst()
                .orElse("");

        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message(errorMessages)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseVO handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleResourceNotFoundException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Không tìm thấy tài nguyên")
                .build();
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseVO handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleFileSizeLimitExceededException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Kích thước file quá lớn")
                .build();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseVO handleMultipartException(MultipartException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleMultipartException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Kích thước file quá lớn")
                .build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseVO handleIOException(IOException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleIOException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Có lỗi xảy ra khi đọc file")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO handleNotFoundException(NoHandlerFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleNotFoundException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Không tìm thấy đường dẫn")
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseVO handleBadCredentialsException(BadCredentialsException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleBadCredentialsException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Tên đăng nhập hoặc mật khẩu không đúng")
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseVO handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUsernameNotFoundException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Không tìm thấy tài khoản")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO handleUnwantedException(Exception e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUnwantedException: {}", e.getMessage());
        return ResponseVO
                .builder()
                .success(Boolean.FALSE)
                .data(null)
                .message("Có lỗi xảy ra, vui lòng thử lại sau!")
                .build();
    }
}
