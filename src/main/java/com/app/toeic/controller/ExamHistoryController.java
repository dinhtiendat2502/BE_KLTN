package com.app.toeic.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserExamHistoryService;
import com.app.toeic.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/exam-history")
public class ExamHistoryController {
    private final UserExamHistoryService userExamHistoryService;
    private final UserService userService;
    private static final String SUCCESS = "Thành công";

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
        var profile = userService.getProfile(request)
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
    public ResponseVO findByUserIdAndExamId(HttpServletRequest request, @PathVariable("userExamHistoryId") String userExamHistoryId) {
        var profile = userService.getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
        var examHistory = userExamHistoryService.findUserExamHistoryByUserIdAndExamId(profile, Integer.parseInt(userExamHistoryId));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message(SUCCESS)
                .build();
    }

}
