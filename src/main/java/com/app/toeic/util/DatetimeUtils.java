package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DatetimeUtils {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public LocalDateTime getFromDate(String dateFrom) {
        if(StringUtils.isBlank(dateFrom)) {
           // return previous 7 days
            return LocalDateTime.now().minusDays(7);
        }
        return LocalDate.parse(dateFrom, formatter).atStartOfDay();
    }

    public LocalDateTime getToDate(String dateTo) {
        if(StringUtils.isBlank(dateTo)) {
            // return current date
            return LocalDateTime.now();
        }
        return LocalDate.parse(dateTo, formatter).atTime(23, 59, 59);
    }
}
