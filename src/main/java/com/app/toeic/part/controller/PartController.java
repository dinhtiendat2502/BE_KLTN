package com.app.toeic.part.controller;


import com.app.toeic.part.payload.PartDTO;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.part.service.PartService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/admin/part")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PartController {
    PartService partService;

    @PostMapping("/list-by-exam")
    public ResponseVO getAllPartByExamId(@RequestBody PartDTO partDto) {
        var parts = partService.getAllPartByExamId(partDto.getExamId());
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
        var part = partService.getPartById(partDto.getPartId());
        part.setPartAudio(partDto.getPartAudio());
        partService.savePart(part);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_PART_SUCCESS")
                .build();
    }
}
