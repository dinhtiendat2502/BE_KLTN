package com.app.toeic.util;

import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@UtilityClass
public class ExcelHelper {
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final XSSFClientAnchor anchor = new XSSFClientAnchor();

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public void writeHeaderLine(String partCode, String[] headerMap) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(partCode);
        var row = sheet.createRow(0);
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);

        int col = 0;
        for (var header : headerMap) {
            createCell(row, col, header, style);
            col++;
        }
    }

    public void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        var cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    @SneakyThrows
    public void writeDataLines(String partCode, com.app.toeic.exam.response.PartResponse part) {
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        // find part by partCode

        var row = sheet.createRow(1);
        var col = 0;
        for (var question : part.getQuestions()) {
            createCell(row, col++, question.getQuestionNumber(), style);
            createCell(row, col++, question.getAnswerA(), style);
            createCell(row, col++, question.getAnswerB(), style);
            createCell(row, col++, question.getAnswerC(), style);
            createCell(row, col++, question.getAnswerD(), style);
            createCell(row, col++, question.getCorrectAnswer(), style);
            createCell(row, col++, question.getTranscript(), style);
            createCell(row, col++, question.getTranslateTranscript(), style);
            var imageInputStream = FileUtils.getInfoFromUrl(question.getQuestionImage());
            var inputImageBytes = IOUtils.toByteArray(imageInputStream.file());
            var inputImagePicture = workbook.addPicture(
                    inputImageBytes,
                    pictureTypeWorkbook(imageInputStream.contentType())
            );
            var drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            anchor.setCol1(col);
            anchor.setRow1(1);
            anchor.setCol2(col + 1);
            anchor.setRow2(2);
            drawing.createPicture(anchor, inputImagePicture);
            col++;
        }
    }
    public void export(HttpServletResponse response, String[] headerMap, com.app.toeic.exam.response.PartResponse part) throws IOException {
        writeHeaderLine(part.getPartCode(), headerMap);
        writeDataLines(part.getPartCode(), part);

        var outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private int pictureTypeWorkbook(String contentType) {
        if (contentType.equals("image/jpeg")) {
            return Workbook.PICTURE_TYPE_JPEG;
        }
        return Workbook.PICTURE_TYPE_PNG;
    }
}
