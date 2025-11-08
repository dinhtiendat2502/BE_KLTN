package com.app.toeic.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@UtilityClass
public class ServerHelper {
    private static final List<String> IP_HEADER_CANDIDATES = List.of(
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
        var clientIp = IP_HEADER_CANDIDATES
                .stream()
                .map(request::getHeader)
                .filter(ipList -> StringUtils.isNotBlank(ipList) && !"unknown".equalsIgnoreCase(ipList))
                .findFirst()
                .map(it -> it.split(",")[0])
                .orElse(request.getRemoteAddr());
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            return "127.0.0.1";
        }
        return clientIp;
    }
}
