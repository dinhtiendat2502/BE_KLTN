package com.app.toeic.exception;

import com.app.toeic.response.ResponseVO;
import com.app.toeic.util.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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
        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return new ResponseVO(Boolean.FALSE, "", "Please check your input");
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO handleUnwantedException(Exception e) {
        log.error("Exception >> GlobalExceptionHandler >> handleUnwantedException: {}", e.getMessage());
        return new ResponseVO(Boolean.FALSE, "", "Something went wrong" + e.getMessage());
    }
}
