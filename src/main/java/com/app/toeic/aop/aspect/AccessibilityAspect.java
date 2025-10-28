package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.Accessibility;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccessibilityAspect {

    @Around(value = "@annotation(accessibility)", argNames = "pjp, accessibility")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final Accessibility accessibility) throws Throwable {
        var activity = accessibility.activity();
        var description = accessibility.description();
        var methodSignature = (MethodSignature) pjp.getSignature();
        var method = methodSignature.getMethod();
        var proceed = ObjectUtils.CONST(null);
        if (method.isAnnotationPresent(Accessibility.class)) {
            var startTime = System.currentTimeMillis();

            proceed = pjp.proceed();
        }
        return proceed;
    }
}
