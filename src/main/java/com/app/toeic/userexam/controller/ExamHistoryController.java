package com.app.toeic.userexam.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.userexam.service.UserExamHistoryService;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam-history")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamHistoryController {
    UserExamHistoryService userExamHistoryService;
    UserService userService;
    private static final String SUCCESS = "Thành công";

    @GetMapping("/find-all")
    public ResponseVO findAll() {
        var examHistories = userExamHistoryService.findAllUserExamHistory();
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistories)
                .message(SUCCESS)
                .build();
    }

    @GetMapping("/find-by-id/{examHistoryId}")
    public ResponseVO findById(@PathVariable("examHistoryId") String examHistoryId) {
        var examHistory = userExamHistoryService.findUserExamHistoryByExamHistoryId(Integer.parseInt(examHistoryId));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message(SUCCESS)
                .build();
    }

    @GetMapping("/my-exam")
    public ResponseVO findByUserId(HttpServletRequest request) {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
        var examHistory = userExamHistoryService.findAllUserExamHistoryByUser(profile);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message(SUCCESS)
                .build();
    }

    @GetMapping("/my-detail/{userExamHistoryId}")
    public ResponseVO findByUserIdAndExamId(
            HttpServletRequest request,
            @PathVariable("userExamHistoryId") String userExamHistoryId
    ) {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
        var examHistory = userExamHistoryService.findUserExamHistoryByUserIdAndExamId(profile, Integer.parseInt(
                userExamHistoryId));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message(SUCCESS)
                .build();
    }

    @GetMapping("detail/{userExamHistoryId}")
    public ResponseVO detail(
            @PathVariable("userExamHistoryId") String userExamHistoryId
    ) {
        var examHistory = userExamHistoryService.findUserExamHistoryByUserIdAndExamId(Integer.parseInt(userExamHistoryId));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message(SUCCESS)
                .build();
    }

}

