package com.app.toeic.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@UtilityClass
public class FileSizeUtils {
    public BigDecimal convertByteToMB(BigInteger byteSize) {
        return new BigDecimal(byteSize).divide(BigDecimal.valueOf((long) 1024 * 1024), 2, RoundingMode.HALF_UP);
    }
}
