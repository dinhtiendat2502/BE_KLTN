package com.app.toeic.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpStatus {
    public final String OK = "200";
    public final String CREATED = "201";
    public final String BAD_REQUEST = "400";
    public final String UNAUTHORIZED = "401";
    public final String NOT_FOUND = "404";
    public final String INTERNAL_SERVER_ERROR = "500";
    public final String SEE_OTHER = "303";

    public final String NOT_FOUND_EXAM = "NOT_FOUND_EXAM";
}
