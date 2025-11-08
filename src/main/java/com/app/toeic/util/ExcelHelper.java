package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;


@UtilityClass
public class ExcelHelper {
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
}
