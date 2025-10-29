package com.app.toeic.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class CookieUtils {

    public static Optional<Cookie> get(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getCookies())
                       .flatMap(cookies -> Arrays.stream(cookies).filter(c -> name.equals(c.getName())).findFirst());
    }

    public static void add(HttpServletResponse response, String name, String value) {
        var cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void add(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void delete(HttpServletRequest request, HttpServletResponse response, String name) {
        Optional.ofNullable(request.getCookies())
                .ifPresent(cookies -> Arrays
                        .stream(cookies)
                        .filter(cookie -> cookie.getName().equals(name))
                        .forEach(cookie -> {
                            cookie.setValue("");
                            cookie.setPath("/");
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }));
    }

}
