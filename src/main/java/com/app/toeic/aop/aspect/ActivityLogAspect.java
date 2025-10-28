package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.ActivityLog;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogAspect {

    @Around(value = "@annotation(log)", argNames = "pjp, log")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final ActivityLog log) throws Throwable {
        return pjp.proceed();
    }
}
