package com.app.toeic.controller.admin;


import com.app.toeic.dto.QuestionDto;
import com.app.toeic.exception.AppException;
import com.app.toeic.model.Question;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.ExcelService;
import com.app.toeic.service.PartService;
import com.app.toeic.service.QuestionService;
import com.app.toeic.util.ExcelHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin/question")
@RequiredArgsConstructor
public class QuestionController {
    private final PartService partService;
    private final ExcelService excelService;
    private final QuestionService questionService;

    @PostMapping("/import-part")
    public ResponseVO importQuestion(@RequestParam("file") MultipartFile file, @RequestParam("partId") Integer partId) throws IOException {
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseVO.builder().success(Boolean.FALSE).message("File không đúng định dạng!").build();
        }
        var part = partService.getPartById(partId);
        var isAddNew = !part.getQuestions().isEmpty() ? Boolean.FALSE : Boolean.TRUE;
        List<Question> listQuestion = !part.getQuestions().isEmpty() ? part.getQuestions().stream().toList() : new ArrayList<>();
        switch (part.getPartCode()) {
            case "PART1" -> {
                listQuestion = excelService.excelToPart1(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART2" -> {
                listQuestion = excelService.excelToPart2(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART3" -> {
                listQuestion = excelService.excelToPart3(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART4" -> {
                listQuestion = excelService.excelToPart4(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART5" -> {
                listQuestion = excelService.excelToPart5(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART6" -> {
                listQuestion = excelService.excelToPart6(file.getInputStream(), part, listQuestion, isAddNew);
            }
            case "PART7" -> {
                listQuestion = excelService.excelToPart7(file.getInputStream(), part, listQuestion, isAddNew);
            }
        }
        questionService.saveAllQuestion(listQuestion);
        return ResponseVO.builder().success(Boolean.TRUE).message("Import thành công!").build();
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
        return ResponseVO.builder().success(Boolean.TRUE).message("Xóa thành công!").build();
    }

    @PatchMapping("/update-question")
    public ResponseVO updateResponseVO(@RequestBody QuestionDto questionDto) {
        var question = questionService.findById(questionDto.getQuestionId());
        if (StringUtils.isNotBlank(questionDto.getQuestionContent()))
            question.setQuestionContent(questionDto.getQuestionContent());
        if (StringUtils.isNotBlank(questionDto.getQuestionImage()))
            question.setQuestionImage(questionDto.getQuestionImage());
        if (StringUtils.isNotBlank(questionDto.getQuestionAudio()))
            question.setQuestionAudio(questionDto.getQuestionAudio());
        if (StringUtils.isNotBlank(questionDto.getParagraph1()))
            question.setParagraph1(questionDto.getParagraph1());
        if (StringUtils.isNotBlank(questionDto.getParagraph2()))
            question.setParagraph2(questionDto.getParagraph2());
        if (StringUtils.isNotBlank(questionDto.getAnswerA()))
            question.setAnswerA(questionDto.getAnswerA());
        if (StringUtils.isNotBlank(questionDto.getAnswerB()))
            question.setAnswerB(questionDto.getAnswerB());
        if (StringUtils.isNotBlank(questionDto.getAnswerC()))
            question.setAnswerC(questionDto.getAnswerC());
        if (StringUtils.isNotBlank(questionDto.getAnswerD()))
            question.setAnswerD(questionDto.getAnswerD());
        if (StringUtils.isNotBlank(questionDto.getCorrectAnswer()))
            question.setCorrectAnswer(questionDto.getCorrectAnswer());
        questionService.saveQuestion(question);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("Cập nhật thành công!")
                .build();
    }

}
