package com.app.toeic.userexam.controller;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import com.app.toeic.util.JsonConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;

import java.text.MessageFormat;
import java.util.List;

@Log
@RestController
@RequestMapping("/user-exam-log")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserExamLogController {
    JedisPooled jedisPooled;
    UserService userService;
    private static final String KEY_EXAM = "answerExam";

    @GetMapping("get-list-answer")
    public Object getListAnswer(@RequestParam("examId") Integer examId, HttpServletRequest request) {
        var profile = userService.getProfile(request)
                                 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));
        var key = jedisPooled.hget(KEY_EXAM, getKeyCacheExam(profile.getUserId(), examId));
        log.info(MessageFormat.format("UserExamLogController >> getListAnswer >> {0}", key));
        if (StringUtils.isBlank(key)) {
            return ResponseVO.builder().success(false).build();
        }
        return ResponseVO
                .builder()
                .success(true)
                .data(JsonConverter.convertToObject(key, AnswerExamUser.class))
                .build();
    }

    @PostMapping("remove-answer/{examId}")
    public Object removeAnswer(@PathVariable("examId") Integer examId, HttpServletRequest request) {
        var profile = userService.getProfile(request)
                                 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));
        var key = jedisPooled.hdel(KEY_EXAM, getKeyCacheExam(profile.getUserId(), examId));
        log.info(MessageFormat.format("UserExamLogController >> getListAnswer >> {0}", key));
        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @PostMapping("save-answer")
    public Object saveCache(HttpServletRequest request, @RequestBody AnswerExamUser answerExamUser) {
        var profile = userService.getProfile(request)
                                 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));
        jedisPooled.hsetObject(
                KEY_EXAM,
                getKeyCacheExam(profile.getUserId(), answerExamUser.getExamId()),
                JsonConverter.convertObjectToJson(answerExamUser)
        );
        return ResponseVO.builder().message("Success").data(profile.getUserId()).build();
    }

    private String getKeyCacheExam(int userId, int examId) {
        return MessageFormat.format(Constant.URL_CACHE_EXAM, userId, examId);
    }


    @Getter
    @Setter
    public static class AnswerExamUser {
        int examId;
        int totalTime;
        int totalLeave;
        int totalOpenNewTab;
        String startTime;
        String endTime;
        List<AnswerUserQuestion> answers;

        @Getter
        @Setter
        static class AnswerUserQuestion {
            int questionId;
            String answer;
            String partCode;
        }
    }
}
