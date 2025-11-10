package com.app.toeic.chart.controller;

import com.app.toeic.exam.response.ExamStatistic;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.user.response.UserAccountRepsonse;
import com.app.toeic.userexam.model.UserExamHistory;
import com.app.toeic.userexam.repo.IUserExamHistoryRepository;
import com.app.toeic.userexam.response.UserExamHistoryVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/statistic")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    IUserExamHistoryRepository iUserExamHistoryRepository;
    IUserAccountRepository iUserAccountRepository;

    @GetMapping("user/exam")
    public Object getStatisticUser(
            @AuthenticationPrincipal UserAccount userAccount
    ) {
        var list = iUserExamHistoryRepository.findUserExamHistoryByUser(userAccount);
        var rs = list.stream()
                     .collect(Collectors.groupingBy(e -> e.getExamDate().toLocalDate()))
                     .entrySet()
                     .stream()
                     .map(entry -> {
                         var numberCorrectAnswer = entry.getValue()
                                                        .stream()
                                                        .mapToInt(UserExamHistoryVO.UserExamHistoryGeneral::getNumberOfCorrectAnswer)
                                                        .sum();
                         var numberQuestion = entry.getValue()
                                                   .stream()
                                                   .mapToInt(UserExamHistoryVO.UserExamHistoryGeneral::getTotalQuestion)
                                                   .sum();
                         var percent = (int) Math.round((double) numberCorrectAnswer / numberQuestion * 100);
                         return new AnalystExamUser(entry.getKey().toString(), percent);
                     })
                     .toList();
        return ResponseVO
                .builder()
                .success(true)
                .data(rs)
                .build();
    }

    @GetMapping("exam/list")
    public Object getTotalUserDoInExam() {
        var rs = ResponseVO.builder().success(true).build();
        var list = iUserExamHistoryRepository.findAll()
                                             .stream()
                                             .collect(Collectors.groupingBy(UserExamHistory::getExam))
                                             .entrySet()
                                             .stream()
                                             .map(entry -> {
                                                 var totalUser = entry.getValue().size();
                                                 var maxScore = entry.getValue()
                                                                     .stream()
                                                                     .mapToInt(UserExamHistory::getTotalScore)
                                                                     .max()
                                                                     .orElse(0);
                                                 var minScore = entry.getValue()
                                                                     .stream()
                                                                     .mapToInt(UserExamHistory::getTotalScore)
                                                                     .min()
                                                                     .orElse(0);
                                                 var percentCorrect = Math.round((double) entry
                                                         .getValue()
                                                         .stream()
                                                         .mapToInt(UserExamHistory::getNumberOfCorrectAnswer)
                                                         .sum() / entry.getValue()
                                                                       .stream()
                                                                       .mapToInt(UserExamHistory::getTotalQuestion)
                                                                       .sum() * 100);
                                                 return new ExamStatistic(
                                                         entry.getKey().getExamId(),
                                                         entry.getKey().getExamName(),
                                                         totalUser,
                                                         maxScore,
                                                         minScore,
                                                         percentCorrect
                                                 );
                                             })
                                             .toList();
        rs.setData(list);
        return rs;
    }

    @GetMapping("exam/detail")
    public Object getDetailExam(@RequestParam("examId") Integer examId) {
        var rs = ResponseVO.builder().success(true).build();
        var list = iUserExamHistoryRepository.findAllByExam(examId);
        rs.setData(list);
        return rs;
    }

    @GetMapping("real-exam")
    public Object getRealExam() {
        var rs = ResponseVO.builder().success(true).build();

        return rs;
    }

    @GetMapping("user/chart")
    public Object getUserChart() {
        var rs = ResponseVO.builder().success(true).build();
        var list = iUserAccountRepository.findAllAccountUser();
        var data = list.stream()
                       .collect(Collectors.groupingBy(UserAccountRepsonse::getUserType))
                       .entrySet()
                       .stream()
                       .map(entry -> {
                           var totalUser = entry.getValue().size();
                           return new StatisticUser(entry.getKey().name(), totalUser);
                       })
                       .toList();
        rs.setData(data);
        return rs;
    }

    public record AnalystExamUser(String examDate, int percent) {
    }

    public record StatisticUser(String userType, int totalUser) {
    }
}
