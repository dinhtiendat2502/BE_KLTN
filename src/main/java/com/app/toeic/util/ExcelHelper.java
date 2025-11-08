package com.app.toeic.util;

import com.app.toeic.part.model.PartEnum;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Log
@UtilityClass
public class ExcelHelper {
    public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private XSSFWorkbook workbook;
    private static final String NULL = StringUtils.EMPTY;
    private final List<XSSFSheet> sheets = new ArrayList<>(7);

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public void writeHeaderLine(String partCode, String[] headerMap) {
        var sheet = workbook.createSheet(partCode);
        sheets.add(sheet);
        var row = sheet.createRow(0);

        var col = new AtomicInteger(0);
        for (var header : headerMap) {
            createCell(row, col, header);
        }
    }

    private void createCell(Row row, AtomicInteger columnCount, Object value) {
        var cell = row.createCell(columnCount.get());
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        columnCount.incrementAndGet();
    }

    @SneakyThrows
    public void writeDataLines(com.app.toeic.exam.response.PartResponse part) {
        var index = getIndexOfSheet(part.getPartCode());
        var sheet = sheets.get(index);

        var rowCount = new AtomicInteger(1);
        for (var question : part.getQuestions()) {
            var row = sheet.createRow(rowCount.get());
            var col = new AtomicInteger(0);
            createCell(row, col, question.getQuestionNumber());
            createCell(row, col, StringUtils.defaultIfBlank(question.getQuestionContent(), NULL));
            createCell(row, col, question.getAnswerA());
            createCell(row, col, question.getAnswerB());
            createCell(row, col, question.getAnswerC());
            createCell(row, col, StringUtils.defaultIfBlank(question.getAnswerD(), NULL));
            createCell(row, col, question.getCorrectAnswer());
            createCell(row, col, StringUtils.defaultIfBlank(question.getTranscript(), NULL));
            createCell(row, col, StringUtils.defaultIfBlank(question.getTranslateTranscript(), NULL));

            if (!"PART7".equalsIgnoreCase(part.getPartCode()) && StringUtils.isNotBlank(question.getQuestionImage())) {
                saveImageToExcel(question.getQuestionImage(), col, rowCount.get(), sheet);
            } else if (question.getHaveMultiImage()) {
                for (var questionImage : question.getQuestionImages()) {
                    saveImageToExcel(questionImage.getQuestionImage(), col, rowCount.get(), sheet);
                }
            }
            rowCount.incrementAndGet();
        }
    }

    private void saveImageToExcel(
            String question,
            AtomicInteger col,
            int rowCount,
            XSSFSheet sheet
    ) throws IOException {
        var imageInputStream = FileUtils.getInfoFromUrl(question);
        var inputImagePicture = workbook.addPicture(
                imageInputStream.imageBytes(),
                pictureTypeWorkbook(imageInputStream.contentType())
        );
        var drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        var anchor = workbook.getCreationHelper().createClientAnchor();
        anchor.setCol1(col.get());
        anchor.setRow1(rowCount);
        anchor.setCol2(col.incrementAndGet());
        anchor.setRow2(rowCount + 1);
        drawing.createPicture(anchor, inputImagePicture);
        sheet.setColumnWidth(col.get(), 20 * 256);
    }

    public void export(
            OutputStream outputStream,
            String[] headerMap,
            List<com.app.toeic.exam.response.PartResponse> parts
    ) throws IOException {
        workbook = new XSSFWorkbook();
        for (var part : parts) {
            writeHeaderLine(part.getPartCode(), headerMap);
            writeDataLines(part);
        }
        workbook.write(outputStream);
        workbook.close();
    }


    private int pictureTypeWorkbook(String contentType) {
        return contentType.equals("image/jpeg") ? Workbook.PICTURE_TYPE_JPEG : Workbook.PICTURE_TYPE_PNG;
    }

    private int getIndexOfSheet(String partCode) {
        return PartEnum.valueOf(partCode).getValue() - 1;
    }
}
