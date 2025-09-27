package com.app.toeic.part.controller;


import com.app.toeic.part.payload.PartDto;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.part.service.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/part")
public class PartController {

    private final PartService partService;

    @PostMapping("/list-by-exam")
    public ResponseVO getAllPartByExamId(@RequestBody PartDto partDto) {
        var parts = partService.getAllPartByExamId(partDto.getExamId());
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(parts)
                .message("Lấy danh sách part thành công!")
                .build();
    }

    @GetMapping("/find-by-id")
    public ResponseVO getPartById(@RequestParam("partId") Integer partId) {
        var part = partService.getPartById(partId);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(part)
                .message("Lấy part thành công!")
                .build();
    }

    @PatchMapping("/update-part")
    public ResponseVO updatePart(@RequestBody PartDto partDto) {
        var part = partService.getPartById(partDto.getPartId());
        part.setPartAudio(partDto.getPartAudio());
        partService.savePart(part);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("Cập nhật part thành công!")
                .build();
    }
}
