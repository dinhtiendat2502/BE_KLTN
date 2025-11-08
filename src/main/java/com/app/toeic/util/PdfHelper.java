package com.app.toeic.util;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

@UtilityClass
public class PdfHelper {
    public void writeCell(PdfPTable table, PdfPCell cell, Font font, Object value, FormatType formatType, Alignment alignment) {
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
        switch (alignment) {
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
        mapValue.forEach((key, value) -> {
            var header = new Paragraph(value, font);
            header.setAlignment(alignConfig.getOrDefault(key, 1));
            if (key == mapValue.size() - 1) {
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
