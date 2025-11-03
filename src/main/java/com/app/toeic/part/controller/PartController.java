package com.app.toeic.part.controller;

import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.part.payload.PartDTO;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@Log
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/part")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PartController {
    PartService partService;
    IExamRepository examRepository;

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
}
