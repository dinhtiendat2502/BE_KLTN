package com.app.toeic.controller;


import com.app.toeic.dto.FinishExamDto;
import com.app.toeic.dto.ListPartDto;
import com.app.toeic.exception.AppException;
import com.app.toeic.model.Exam;
import com.app.toeic.model.Question;
import com.app.toeic.model.UserAnswer;
import com.app.toeic.model.UserExamHistory;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExamService;
import com.app.toeic.service.UserAnswerService;
import com.app.toeic.service.UserExamHistoryService;
import com.app.toeic.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamRestController {
    private final ExamService examService;
    private final UserService userService;
    private final UserExamHistoryService userExamHistoryService;
    private final UserAnswerService userAnswerService;

    @GetMapping("/list")
    public ResponseVO getAllExams() {
        return examService.getAllExam();
    }

    @GetMapping("/list-by-topic/{topicId}")
    public ResponseVO getAllExamByTopic(@PathVariable String topicId) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examService.getAllExamByTopic(Integer.parseInt(topicId)))
                .message("Lấy danh sách đề thi theo chủ đề thành công")
                .build();
    }

    @GetMapping("/find-by-id/{examId}")
    public ResponseVO findById(@PathVariable("examId") String examId) {
        var exam = examService
                .findExamByExamId(Integer.parseInt(examId))
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("Lấy đề thi thành công")
                .build();
    }

    @GetMapping("/find-full-question/{examId}")
    public ResponseVO findFullQuestion(@PathVariable("examId") String examId) {
        var exam = examService
                .findExamWithFullQuestion(Integer.parseInt(examId))
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("Lấy đề thi thành công")
                .build();
    }

    @PostMapping("/finish-exam")
    public ResponseVO finishExam(HttpServletRequest request, @RequestBody FinishExamDto finishExamDto) {
        var user = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
        var examHasFullQuestionAnswer = examService
                .findExamFullQuestionWithAnswer(finishExamDto.getExamId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        var userExamHistory = UserExamHistory
                .builder()
                .isDone(finishExamDto.getIsDone())
                .isFullTest(finishExamDto.getIsFullTest())
                .listPart(finishExamDto.getListPart())
                .timeToDoExam(finishExamDto.getTotalTime())
                .timeRemaining(finishExamDto.getTimeRemaining())
                .totalQuestion(finishExamDto.getTotalQuestion())
                .numberOfCorrectAnswer(0)
                .numberOfWrongAnswer(0)
                .numberOfNotAnswer(0)
                .numberOfCorrectAnswerPart1(0)
                .numberOfCorrectAnswerPart2(0)
                .numberOfCorrectAnswerPart3(0)
                .numberOfCorrectAnswerPart4(0)
                .numberOfCorrectAnswerPart5(0)
                .numberOfCorrectAnswerPart6(0)
                .numberOfCorrectAnswerPart7(0)
                .numberOfCorrectListeningAnswer(0)
                .numberOfWrongListeningAnswer(0)
                .numberOfCorrectReadingAnswer(0)
                .numberOfWrongReadingAnswer(0)
                .totalScore(0)
                .totalScoreReading(0)
                .totalScoreListening(0)
                .exam(Exam
                              .builder()
                              .examId(examHasFullQuestionAnswer.getExamId())
                              .build())
                .user(user)
                .build();

        var returnUserExamHistory = userExamHistoryService.save(userExamHistory);
        AtomicInteger numberOfCorrectAnswer = new AtomicInteger();
        AtomicInteger numberOfWrongAnswer = new AtomicInteger();
        AtomicInteger numberOfNotAnswer = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart1 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart2 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart3 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart4 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart5 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart6 = new AtomicInteger();
        AtomicInteger numberOfCorrectAnswerPart7 = new AtomicInteger();
        AtomicInteger numberOfCorrectListeningAnswer = new AtomicInteger();
        AtomicInteger numberOfWrongListeningAnswer = new AtomicInteger();
        AtomicInteger numberOfCorrectReadingAnswer = new AtomicInteger();
        AtomicInteger numberOfWrongReadingAnswer = new AtomicInteger();
        var listAnswer = finishExamDto
                .getAnswers()
                .stream()
                .map(asw -> {
                    Question question = Question
                            .builder()
                            .questionId(asw.getQuestionId())
                            .build();
                    var userAnswer = UserAnswer
                            .builder()
                            .selectedAnswer(asw.getAnswer())
                            .question(question)
                            .userExamHistory(returnUserExamHistory)
                            .build();

                    var correctAnswer = examService.findCorrectAnswer(examHasFullQuestionAnswer, asw.getQuestionId());
                    boolean isCorrect = Objects.equals(asw.getAnswer(), correctAnswer);
                    userAnswer.setIsCorrect(isCorrect);
                    if (StringUtils.isEmpty(asw.getAnswer()) || asw.getAnswer() == null) {
                        numberOfNotAnswer.incrementAndGet();
                    }
                    String partCode = asw.getPartCode();
                    if (!isCorrect) {
                        numberOfWrongAnswer.incrementAndGet();
                        switch (partCode) {
                            case "PART1", "PART4", "PART2", "PART3" -> numberOfWrongListeningAnswer.incrementAndGet();
                            case "PART5", "PART6", "PART7" -> numberOfWrongReadingAnswer.incrementAndGet();
                            default -> {
                            }
                        }
                    } else {
                        numberOfCorrectAnswer.incrementAndGet();
                        switch (partCode) {
                            case "PART1" -> {
                                numberOfCorrectAnswerPart1.incrementAndGet();
                                numberOfCorrectListeningAnswer.incrementAndGet();
                            }
                            case "PART2" -> {
                                numberOfCorrectAnswerPart2.incrementAndGet();
                                numberOfCorrectListeningAnswer.incrementAndGet();
                            }
                            case "PART3" -> {
                                numberOfCorrectAnswerPart3.incrementAndGet();
                                numberOfCorrectListeningAnswer.incrementAndGet();
                            }
                            case "PART4" -> {
                                numberOfCorrectAnswerPart4.incrementAndGet();
                                numberOfCorrectListeningAnswer.incrementAndGet();
                            }
                            case "PART5" -> {
                                numberOfCorrectAnswerPart5.incrementAndGet();
                                numberOfCorrectReadingAnswer.incrementAndGet();
                            }
                            case "PART6" -> {
                                numberOfCorrectAnswerPart6.incrementAndGet();
                                numberOfCorrectReadingAnswer.incrementAndGet();
                            }
                            case "PART7" -> {
                                numberOfCorrectAnswerPart7.incrementAndGet();
                                numberOfCorrectReadingAnswer.incrementAndGet();
                            }
                            default -> {
                            }
                        }
                    }
                    return userAnswer;
                })
                .toList();
        userAnswerService.saveAll(listAnswer);
        returnUserExamHistory.setNumberOfCorrectAnswer(numberOfCorrectAnswer.get());
        returnUserExamHistory.setNumberOfWrongAnswer(numberOfWrongAnswer.get());
        returnUserExamHistory.setNumberOfNotAnswer(numberOfNotAnswer.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart1(numberOfCorrectAnswerPart1.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart2(numberOfCorrectAnswerPart2.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart3(numberOfCorrectAnswerPart3.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart4(numberOfCorrectAnswerPart4.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart5(numberOfCorrectAnswerPart5.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart6(numberOfCorrectAnswerPart6.get());
        returnUserExamHistory.setNumberOfCorrectAnswerPart7(numberOfCorrectAnswerPart7.get());
        returnUserExamHistory.setNumberOfCorrectListeningAnswer(numberOfCorrectListeningAnswer.get());
        returnUserExamHistory.setNumberOfWrongListeningAnswer(numberOfWrongListeningAnswer.get());
        returnUserExamHistory.setNumberOfCorrectReadingAnswer(numberOfCorrectReadingAnswer.get());
        returnUserExamHistory.setNumberOfWrongReadingAnswer(numberOfWrongReadingAnswer.get());
        returnUserExamHistory.setTotalScore(userExamHistoryService.calculateScoreListening(
                numberOfCorrectListeningAnswer.get()) + userExamHistoryService.calculateScoreReading(
                numberOfCorrectReadingAnswer.get()));
        returnUserExamHistory.setTotalScoreListening(
                userExamHistoryService.calculateScoreListening(numberOfCorrectListeningAnswer.get()));
        returnUserExamHistory.setTotalScoreReading(
                userExamHistoryService.calculateScoreReading(numberOfCorrectReadingAnswer.get()));
        userExamHistoryService.save(returnUserExamHistory);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(returnUserExamHistory.getUserExamHistoryId())
                .message("Nộp bài thành công")
                .build();
    }

    @PostMapping("/find-exam-practice/{examId}")
    public ResponseVO findExamPractice(@PathVariable("examId") String examId, @RequestBody ListPartDto listPartDto) {
        var exam = examService
                .findExamPractice(Integer.parseInt(examId), listPartDto.getListPart())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message("Lấy đề thi thành công")
                .build();
    }

}
