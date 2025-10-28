package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class ServerHelper {
    private static final List<String> IP_HEADER_CANDIDATES = Arrays.asList(
            "CF-Connecting-IP",
            "TRUE-CLIENT-IP",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    );

    public static String getClientIp() {
        var requestContext = RequestContextHolder.getRequestAttributes();
        if (requestContext == null) {
            return StringUtils.EMPTY;
        }
        var request = ((ServletRequestAttributes) requestContext).getRequest();
        return IP_HEADER_CANDIDATES.stream().map(request::getHeader).filter(ipList -> StringUtils.isNotBlank(ipList) && !"unknown".equalsIgnoreCase(ipList))
                .findFirst().map(it -> it.split(",")[0]).orElse(request.getRemoteAddr());
    }
}
