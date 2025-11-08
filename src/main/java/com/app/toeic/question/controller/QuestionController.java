package com.app.toeic.question.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.question.model.Question;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.ExcelService;
import com.app.toeic.part.service.PartService;
import com.app.toeic.question.payload.QuestionDTO;
import com.app.toeic.question.service.QuestionService;
import com.app.toeic.util.ExcelHelper;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

@Log
@RestController
@RequestMapping("/admin/question")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class QuestionController {
    PartService partService;
    ExcelService excelService;
    QuestionService questionService;

    @PostMapping(value = "/import-part", consumes = {"multipart/form-data"})
    @SneakyThrows
    public ResponseVO importQuestion(@RequestParam("file") MultipartFile file, @RequestParam("partId") Integer partId) {
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("File không đúng định dạng!")
                    .build();
        }
        var part = partService.getPartById(partId);
        var isAddNew = part
                .getQuestions()
                .isEmpty();
        var listQuestion = !part
                .getQuestions()
                .isEmpty() ? part
                .getQuestions()
                .stream()
                .toList() : new ArrayList<Question>();
        listQuestion = switch (part.getPartCode()) {
            case "PART1" -> excelService.excelToPart1(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART2" -> excelService.excelToPart2(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART3" -> excelService.excelToPart3(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART4" -> excelService.excelToPart4(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART5" -> excelService.excelToPart5(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART6" -> excelService.excelToPart6(file.getInputStream(), part, listQuestion, isAddNew);
            case "PART7" -> excelService.excelToPart7(file.getInputStream(), part, listQuestion, isAddNew);
            default -> throw new AppException(HttpStatus.NOT_FOUND, "PART_NOT_FOUND");
        };
        questionService.saveAllQuestion(listQuestion);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("IMPORT_SUCCESS")
                .build();
    }

    @GetMapping("/list-by-part")
    public ResponseVO getAllQuestionByPartId(@RequestParam Integer partId) {
        var part = partService.getPartById(partId);
        return questionService.getAllQuestionByPartId(part);
    }

    @DeleteMapping("/delete-all-by-part")
    public ResponseVO deleteAllQuestionByPartId(@RequestParam Integer partId) {
        var part = partService.getPartById(partId);
        questionService.removeQuestionByPart(part);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("DELETE_ALL_QUESTION_BY_PART_SUCCESS")
                .build();
    }

    @PatchMapping("/update-question")
    public ResponseVO updateResponseVO(@RequestBody QuestionDTO questionDto) {
        var question = questionService.findById(questionDto.getQuestionId());
        Map<Consumer<String>, String> updates = Map.of(
                question::setQuestionContent, questionDto.getQuestionContent(),
                question::setQuestionImage, questionDto.getQuestionImage(),
                question::setQuestionAudio, questionDto.getQuestionAudio(),
                question::setParagraph1, questionDto.getParagraph1(),
                question::setParagraph2, questionDto.getParagraph2(),
                question::setAnswerA, questionDto.getAnswerA(),
                question::setAnswerB, questionDto.getAnswerB(),
                question::setAnswerC, questionDto.getAnswerC(),
                question::setAnswerD, questionDto.getAnswerD(),
                question::setCorrectAnswer, questionDto.getCorrectAnswer()
        );

        updates.forEach((setter, value) -> {
            if (StringUtils.isNotBlank(value)) {
                setter.accept(value);
            }
        });
        questionService.saveQuestion(question);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_QUESTION_SUCCESS")
                .build();
    }

}
