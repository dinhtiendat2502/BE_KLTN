package com.app.toeic.part.controller;

import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.part.model.Part;
import com.app.toeic.part.payload.PartDTO;
import com.app.toeic.part.repo.IPartRepository;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.ExcelHelper;
import com.app.toeic.util.JsonConverter;
import com.google.protobuf.Message;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/part")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PartController {
    PartService partService;
    IExamRepository examRepository;
    IPartRepository iPartRepository;

    @PostMapping("import-part-1")
    public Object importPart1(@RequestParam("file") MultipartFile file) {
        return null;
    }


    @PostMapping("/list-by-exam")
    public ResponseVO getAllPartByExamId(@RequestBody PartDTO partDto) {
        var parts = partService.getAllPartByExamId(partDto.examId());
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(parts)
                .message("GET_ALL_PART_BY_EXAM_ID_SUCCESS")
                .build();
    }

    @GetMapping("/find-by-id")
    public ResponseVO getPartById(@RequestParam("partId") Integer partId) {
        var part = partService.getPartById(partId);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(part)
                .message("GET_PART_BY_ID_SUCCESS")
                .build();
    }

    @PatchMapping("/update-part")
    public ResponseVO updatePart(@RequestBody PartDTO partDto) {
        var part = partService.getPartById(partDto.partId());
        part.setPartAudio(partDto.partAudio());
        partService.savePart(part);
        var exam = examRepository.findById(partDto.examId()).orElse(null);
        if (exam != null) {
            switch (part.getPartCode()) {
                case "PART1" -> exam.setAudioPart1(partDto.partAudio());
                case "PART2" -> exam.setAudioPart2(partDto.partAudio());
                case "PART3" -> exam.setAudioPart3(partDto.partAudio());
                case "PART4" -> exam.setAudioPart4(partDto.partAudio());
                default -> log.warning(MessageFormat.format(
                        "PartController -> updatePart -> Invalid part code -> param {0}",
                        JsonConverter.convertObjectToJson(partDto)
                ));
            }
            examRepository.save(exam);
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_PART_SUCCESS")
                .build();
    }

    public String[] headers = {
            "Question Number",
            "Answer A",
            "Answer B",
            "Answer C",
            "Answer D",
            "Correct Answer",
            "Transcript",
            "Translate Transcript",
            "Question Image"
    };

    @GetMapping("export-part")
    public void exportPart1ToExcel(@RequestParam("partId") Integer partId, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        var dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        var currentDateTime = dateFormatter.format(new Date());
        var headerKey = "Content-Disposition";
        var headerValue = STR."attachment; filename=users_\{currentDateTime}.xlsx";
        response.setHeader(headerKey, headerValue);
        var part = iPartRepository.findPartById(partId);
        part.ifPresent(p -> {
            try {
                ExcelHelper.export(response, headers, p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
