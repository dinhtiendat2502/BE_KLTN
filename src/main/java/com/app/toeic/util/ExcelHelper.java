package com.app.toeic.util;

import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;


@Log
@UtilityClass
public class ExcelHelper {
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

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

        int rowCount = 1;
        for (var question : part.getQuestions()) {
            var row = sheet.createRow(rowCount);
            var col = 0;
            createCell(row, col++, question.getQuestionNumber(), style);
            createCell(row, col++, question.getAnswerA(), style);
            createCell(row, col++, question.getAnswerB(), style);
            createCell(row, col++, question.getAnswerC(), style);
            if(!"PART2".equalsIgnoreCase(part.getPartCode())) {
                createCell(row, col++, question.getAnswerD(), style);
            }
            createCell(row, col++, question.getCorrectAnswer(), style);
            createCell(row, col++, question.getTranscript(), style);
            createCell(row, col++, question.getTranslateTranscript(), style);

            if(!"PART2".equalsIgnoreCase(part.getPartCode())) {
                var imageInputStream = FileUtils.getInfoFromUrl(question.getQuestionImage());
                var inputImagePicture = workbook.addPicture(
                        imageInputStream.imageBytes(),
                        pictureTypeWorkbook(imageInputStream.contentType())
                );
                var drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                var anchor = workbook.getCreationHelper().createClientAnchor();
                anchor.setCol1(col);
                anchor.setRow1(rowCount);
                anchor.setCol2(col + 1);
                anchor.setRow2(rowCount + 1);
                drawing.createPicture(anchor, inputImagePicture);
                sheet.setColumnWidth(col, 20 * 256);
            }
            rowCount++;
        }
    }

    public void export(
            HttpServletResponse response,
            String[] headerMap,
            com.app.toeic.exam.response.PartResponse part
    ) throws IOException {
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

    private byte[] loadImageFromUrl(InputStream inputStream) {
        try (var baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            log.log(Level.WARNING, "ExcelHelper -> loadImageFromUrl -> error: ", e);
            return null;
        }
    }
}
