package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.ActivityLog;
import com.app.toeic.user.model.UserAccountLog;
import com.app.toeic.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Level;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogAspect {

    @Around(value = "@annotation(activityLog)", argNames = "pjp, activityLog")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final ActivityLog activityLog) throws Throwable {
        var startTime = System.currentTimeMillis();
        var methodSignature = pjp.getSignature();
        var params = pjp.getArgs();
        var result = ObjectUtils.CONST(null);
        try {
            result = pjp.proceed();
            return result;
        } catch (Exception e) {
            log.log(Level.WARNING, "AccessibilityAspect >> aroundAspect >> Exception: {0}", e);
            throw e;
        } finally {
            var userAccountLog = getUserAccountLog(params, activityLog.activity(), activityLog.description(), result);
            log.info(MessageFormat.format(
                    "ActivityLogAspect >> aroundAspect >> Finally >> action: {0} >> time: {1} ms",
                    activityLog.activity(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private UserAccountLog getUserAccountLog(Object[] params, String action, String description, Object result) {
        var rs = UserAccountLog
                .builder();
        switch (action){
            case Constant.FORGOT_PASSWORD -> {

            }
            case Constant.RESET_PASSWORD -> {

            }
            case Constant.UPDATE_PASSWORD -> {

            }
            case Constant.UPDATE_AVATAR -> {

            }
            case Constant.UPDATE_PROFILE -> {

            }
        }
        return rs.build();
    }
}
