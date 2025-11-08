package com.app.toeic.exam.controller;


import com.app.toeic.exam.payload.FinishExamDTO;
import com.app.toeic.part.payload.ListPartDTO;
import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.question.model.Question;
import com.app.toeic.score.repo.ICalculateScoreRepository;
import com.app.toeic.userexam.model.UserAnswer;
import com.app.toeic.userexam.model.UserExamHistory;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.userexam.service.UserAnswerService;
import com.app.toeic.userexam.service.UserExamHistoryService;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/exam")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;
    UserService userService;
    UserExamHistoryService userExamHistoryService;
    UserAnswerService userAnswerService;
    ICalculateScoreRepository calculateScoreRepository;
    private static final String EXAM_NOT_FOUND = "EXAM_NOT_FOUND";
    private static final String GET_EXAM_SUCCESS = "GET_EXAM_SUCCESS";

    @GetMapping("/list")
    public Object getAllExams() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examService.getAllExam())
                .message(GET_EXAM_SUCCESS)
                .build();
    }

    @GetMapping("/list-by-topic/{topicId}")
    public ResponseVO getAllExamByTopic(@PathVariable String topicId) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(examService.getAllExamByTopic(Integer.parseInt(topicId)))
                .message(GET_EXAM_SUCCESS)
                .build();
    }

    @GetMapping("/find-by-id/{examId}")
    public ResponseVO findById(@PathVariable("examId") String examId) {
        var exam = examService
                .findExamByExamId(Integer.parseInt(examId))
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, EXAM_NOT_FOUND));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message(GET_EXAM_SUCCESS)
                .build();
    }

    @GetMapping("/find-full-question/{examId}")
    public ResponseVO findFullQuestion(@PathVariable("examId") String examId) {
        var exam = examService
                .findExamWithFullQuestion(Integer.parseInt(examId))
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, EXAM_NOT_FOUND));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message(GET_EXAM_SUCCESS)
                .build();
    }

    @PostMapping("/finish-exam")
    public ResponseVO finishExam(HttpServletRequest request, @RequestBody FinishExamDTO finishExamDto) {
        var user = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
        var examHasFullQuestionAnswer = examService
                .findExamFullQuestionWithAnswer(finishExamDto.getExamId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, EXAM_NOT_FOUND));
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
                .totalLeave(finishExamDto.getTotalLeave())
                .totalOpenNewTab(finishExamDto.getTotalOpenNewTab())
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
        var numberOfCorrectAnswer = new AtomicInteger();
        var numberOfWrongAnswer = new AtomicInteger();
        var numberOfNotAnswer = new AtomicInteger();
        var numberOfCorrectAnswerPart1 = new AtomicInteger();
        var numberOfCorrectAnswerPart2 = new AtomicInteger();
        var numberOfCorrectAnswerPart3 = new AtomicInteger();
        var numberOfCorrectAnswerPart4 = new AtomicInteger();
        var numberOfCorrectAnswerPart5 = new AtomicInteger();
        var numberOfCorrectAnswerPart6 = new AtomicInteger();
        var numberOfCorrectAnswerPart7 = new AtomicInteger();
        var numberOfCorrectListeningAnswer = new AtomicInteger();
        var numberOfWrongListeningAnswer = new AtomicInteger();
        var numberOfCorrectReadingAnswer = new AtomicInteger();
        var numberOfWrongReadingAnswer = new AtomicInteger();
        var listAnswer = finishExamDto
                .getAnswers()
                .stream()
                .map(asw -> {
                    var question = Question
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
                    var isCorrect = Objects.equals(asw.getAnswer(), correctAnswer);
                    userAnswer.setIsCorrect(isCorrect);
                    if (StringUtils.isBlank(asw.getAnswer())) {
                        numberOfNotAnswer.incrementAndGet();
                    }
                    var partCode = asw.getPartCode();
                    if (!isCorrect) {
                        numberOfWrongAnswer.incrementAndGet();
                        switch (partCode) {
                            case "PART1", "PART4", "PART2", "PART3" -> numberOfWrongListeningAnswer.incrementAndGet();
                            case "PART5", "PART6", "PART7" -> numberOfWrongReadingAnswer.incrementAndGet();
                            default -> log.info("ExamController >> finishExam >> PART NOT FOUND");
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
                            default -> log.info("ExamController >> finishExam >> PART NOT FOUND");
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
        var listScore = calculateScoreRepository.findAllByTotalQuestion(returnUserExamHistory.getNumberOfCorrectReadingAnswer(), returnUserExamHistory.getNumberOfCorrectListeningAnswer());
        returnUserExamHistory.setTotalScoreListening(listScore.getLast().getListening());
        returnUserExamHistory.setTotalScoreReading(listScore.getFirst().getReading());
        returnUserExamHistory.setTotalScore(returnUserExamHistory.getTotalScoreListening() + returnUserExamHistory.getTotalScoreReading());
        userExamHistoryService.save(returnUserExamHistory);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(returnUserExamHistory.getUserExamHistoryId())
                .message("FINISH_EXAM_SUCCESS")
                .build();
    }

    @PostMapping("/find-exam-practice/{examId}")
    public ResponseVO findExamPractice(@PathVariable("examId") String examId, @RequestBody ListPartDTO listPartDto) {
        var exam = examService
                .findExamPractice(Integer.parseInt(examId), listPartDto.getListPart())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, EXAM_NOT_FOUND));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(exam)
                .message(GET_EXAM_SUCCESS)
                .build();
    }

}
