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
    static final List<String> DEFAULT_HEADER = Arrays.asList("X-Forwarded-For",
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

    public String getCurrentUserIp() {
        var defaultIp = "0.0.0.0";
        var requestAttributes = Optional.ofNullable(
                (ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()));
        return requestAttributes
                .map(servletRequestAttributes -> DEFAULT_HEADER
                        .stream()
                        .map(header -> {
                            var ipList = servletRequestAttributes
                                    .getRequest()
                                    .getHeader(header);
                            if (StringUtils.isNoneBlank(ipList) && !"unknown".equalsIgnoreCase(ipList)) {
                                return Arrays
                                        .stream(ipList.split(","))
                                        .findFirst()
                                        .orElse(defaultIp);
                            }
                            return defaultIp;
                        })
                        .findFirst()
                        .orElse(defaultIp))
                .orElse(defaultIp);

    }
}
