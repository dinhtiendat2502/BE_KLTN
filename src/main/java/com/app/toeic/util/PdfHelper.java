package com.app.toeic.util;


import lombok.Data;
import lombok.NoArgsConstructor;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;
@Data
@NoArgsConstructor
public class PdfHelper {
    public void writeCell(PdfPTable table, PdfPCell cell, Font font, Object value, FormatType formatType, Alignment alighment) {
        switch (formatType) {
            case TEXT -> cell.setPhrase(new Phrase(value.toString(), font));
            case BIG_DECIMAL -> {
                BigDecimal number = (BigDecimal) value;
                cell.setPhrase(new Phrase("", font));
            }
            default -> {
                return;
            }
        }
        switch (alighment) {
            case RIGHT -> cell.setHorizontalAlignment(2);
            case LEFT -> cell.setHorizontalAlignment(0);
            default -> cell.setHorizontalAlignment(1);
        }
        table.addCell(cell);
    }


    public int generateHeaderReportInfo(Document document, Map<Integer, String> mapValue, Map<Integer, Integer> alignConfig) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setSize(10);
        font.setColor(Color.BLACK);
        font.setStyle(Font.BOLD);
        mapValue.entrySet().forEach(item -> {
            var header = new Paragraph(item.getValue(), font);
            header.setAlignment(alignConfig.getOrDefault(item.getKey(), 1));
            if (item.getKey() == mapValue.size() - 1) {
                header.setSpacingAfter(10);
            }
            document.add(header);
        });
        return 0;
    }
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }
    public enum FormatType {
        TEXT,
        BIG_DECIMAL
    }
}
