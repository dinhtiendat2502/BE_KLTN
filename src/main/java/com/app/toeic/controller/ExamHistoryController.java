package com.app.toeic.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserExamHistoryService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/exam-history")
public class ExamHistoryController {
    private final UserExamHistoryService userExamHistoryService;

    @GetMapping("/find-by-id/{examHistoryId}")
    public ResponseVO findById(@PathVariable("examHistoryId") String examHistoryId) {
        var examHistory = userExamHistoryService.findUserExamHistoryByExamHistoryId(Integer.parseInt(examHistoryId));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examHistory)
                .message("Lấy lịch sử thi thành công")
                .build();
    }

}
