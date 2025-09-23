package com.app.toeic.util;

import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@UtilityClass
public class URLHelper {
    public static String replacer(String data) throws UnsupportedEncodingException {
        StringBuilder tempBuffer = getStringBuilder(data);
        data = tempBuffer.toString();
        data = URLDecoder.decode(data
                                         .replace("<percentage>", "%")
                                         .replace("<plus>", "+"), "UTF-8");
        return data;
    }

    public static String replaceByRegex(String data) {
        return data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
    }

    private static StringBuilder getStringBuilder(String data) {
        StringBuilder tempBuffer = new StringBuilder();
        int incrementor = 0;
        int dataLength = data.length();
        while (incrementor < dataLength) {
            char charecterAt = data.charAt(incrementor);
            if (charecterAt == '%') {
                tempBuffer.append("<percentage>");
            } else if (charecterAt == '+') {
                tempBuffer.append("<plus>");
            } else {
                tempBuffer.append(charecterAt);
            }
            incrementor++;
        }
        return tempBuffer;
    }
}
