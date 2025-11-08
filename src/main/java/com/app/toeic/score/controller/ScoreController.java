package com.app.toeic.score.controller;


import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.score.model.CalculateScore;
import com.app.toeic.score.repo.ICalculateScoreRepository;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;


@RestController
@RequestMapping("/score")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ScoreController {
    ICalculateScoreRepository calculateScoreRepository;

    @GetMapping("/init")
    public ResponseVO initScore() {
        calculateScoreRepository.deleteAll();
        var initList = IntStream
                .range(0, 101)
                .mapToObj(CalculateScore::from)
                .toList();
        calculateScoreRepository.saveAll(initList);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("INIT_LIST_SCORE_SUCCESS")
                .build();
    }

    @GetMapping("/list")
    public ResponseVO findAll() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(calculateScoreRepository.findAll())
                .message("GET_LIST_SCORE_SUCCESS")
                .build();
    }

    @GetMapping("cal")
    public Object calculateScore(
            @RequestParam("totalQuestionReading") String totalQuestionReading,
            @RequestParam("totalQuestionListening") String totalQuestionListening
    ) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(calculateScoreRepository.findAllByTotalQuestion(
                        Integer.parseInt(totalQuestionReading),
                        Integer.parseInt(totalQuestionListening)
                ))
                .message("CALCULATE_SCORE_SUCCESS")
                .build();
    }

    @PatchMapping("/update")
    public ResponseVO updateScore(@RequestBody CalculateScore calculateScore) {
        calculateScoreRepository.save(calculateScore);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_SCORE_SUCCESS")
                .build();
    }

    @PatchMapping("update-all")
    public ResponseVO updateAllScore(@RequestBody List<CalculateScore> calculateScoreList) {
        calculateScoreRepository.saveAll(calculateScoreList);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("UPDATE_ALL_SCORE_SUCCESS")
                .build();
    }

    @PostMapping(value = "import-file-score", consumes = {"multipart/form-data"})
    public ResponseVO importFileScore(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) return new ResponseVO(Boolean.FALSE, "", "FILE_IS_NULL");

        var list = calculateScoreRepository.findAll();
        if (CollectionUtils.isEmpty(list)) {
            list = IntStream.range(0, 101).mapToObj(CalculateScore::from).toList();
        }

        try (var workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() != 101) {
                throw new AppException(HttpStatus.SEE_OTHER, "FILE_INVALID");
            }

            final var finalList = list;
            sheet.forEach(currentRow -> {
                var rowNumber = currentRow.getRowNum();
                var calculateScore = finalList.get(rowNumber);
                currentRow.forEach(column -> {
                    var columnIndex = column.getColumnIndex();
                    if (columnIndex == 1) {
                        calculateScore.setScoreListening((int) column.getNumericCellValue());
                    } else if (columnIndex == 2) {
                        calculateScore.setScoreReading((int) column.getNumericCellValue());
                    }
                });
            });
            calculateScoreRepository.saveAll(finalList);
        }
        return ResponseVO.builder()
                         .success(Boolean.TRUE)
                         .message("IMPORT_FILE_SCORE_SUCCESS")
                         .build();
    }
}


