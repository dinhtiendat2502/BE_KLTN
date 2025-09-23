package com.app.toeic.controller.admin;


import com.app.toeic.exception.AppException;
import com.app.toeic.model.CalculateScore;
import com.app.toeic.model.Question;
import com.app.toeic.repository.ICalculateScoreRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/score")
public class ScoreController {
    private final ICalculateScoreRepository calculateScoreRepository;

    @GetMapping("/init")
    public ResponseVO initScore() {
        calculateScoreRepository.deleteAll();
        var initList = IntStream
                .range(0, 101)
                .mapToObj(i -> CalculateScore
                        .builder()
                        .totalQuestion(i)
                        .scoreListening(0)
                        .scoreReading(0)
                        .build())
                .toList();
        calculateScoreRepository.saveAll(initList);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("OK")
                .build();
    }

    @GetMapping("/list")
    public ResponseVO findAll() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(calculateScoreRepository.findAll())
                .message("OK")
                .build();
    }

    @PatchMapping("/update")
    public ResponseVO updateScore(@RequestBody CalculateScore calculateScore) {
        calculateScoreRepository.save(calculateScore);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("OK")
                .build();
    }

    @PatchMapping("update-all")
    public ResponseVO updateAllScore(@RequestBody List<CalculateScore> calculateScoreList) {
        calculateScoreRepository.saveAll(calculateScoreList);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("OK")
                .build();
    }

    @PostMapping("import-file-score")
    public ResponseVO importFileScore(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null) return new ResponseVO(Boolean.FALSE, "", "FILE_IS_NULL");
        var list = calculateScoreRepository.findAll();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheet("Sheet1");
        Iterator<Row> rows = sheet.iterator();
        if (sheet.getPhysicalNumberOfRows() != 101) {
            throw new AppException(HttpStatus.SEE_OTHER, "FILE_INVALID");
        }
        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Iterator<Cell> cellsInRow = currentRow.iterator();
            var calculateScore = list.get(rowNumber);
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                int columnIndex = currentCell.getColumnIndex();
                if (columnIndex == 1) {
                    calculateScore.setScoreListening((int) currentCell.getNumericCellValue());
                } else if (columnIndex == 2) {
                    calculateScore.setScoreReading((int) currentCell.getNumericCellValue());
                }
            }
            rowNumber++;
        }
        workbook.close();
        calculateScoreRepository.saveAll(list);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("OK")
                .build();
    }

}
