package com.app.toeic.aop.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationLog {
    String activity() default StringUtils.EMPTY;
    String description() default StringUtils.EMPTY;
}
