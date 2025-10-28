package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.AuthenticationLog;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.model.UserAccountLog;
import com.app.toeic.user.payload.LoginDTO;
import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.user.repo.IUserAccountLogRepository;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.util.ServerHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationLogAspect {
    IUserAccountLogRepository iUserAccountLogRepository;
    IUserAccountRepository iUserAccountRepository;

    @Around(value = "@annotation(authLog)", argNames = "pjp, authLog")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final AuthenticationLog authLog) throws Throwable {
        var activity = authLog.activity();
        var methodSignature = (MethodSignature) pjp.getSignature();
        var method = methodSignature.getMethod();
        var className = pjp.getTarget().getClass().getName();
        var methodName = method.getName();
        var args = pjp.getArgs();
        var ipAddr = ServerHelper.getClientIp();
        var description = MessageFormat.format("{0}.{1} >> {2}", className, methodName, activity);
        var startTime = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } catch (Exception e) {
            log.log(Level.WARNING, "AccessibilityAspect >> aroundAspect >> Exception: ", e);
            throw e;
        } finally {
            var user = Optional.<UserAccount>empty();
            if (args.length > 0) {
                user = switch (args[0]) {
                    case LoginDTO loginDTO -> iUserAccountRepository.findByEmail(loginDTO.getEmail());
                    case LoginSocialDTO socialDTO -> iUserAccountRepository.findByEmail(socialDTO.getEmail());
                    case String email -> iUserAccountRepository.findByEmail(email);
                    default -> Optional.empty();
                };
            }
            var logUser = UserAccountLog.builder()
                                        .action(activity)
                                        .description(description)
                                        .lastIpAddress(ipAddr)
                                        .userAccount(user.orElse(null))
                                        .build();
            iUserAccountLogRepository.save(logUser);
            log.log(
                    Level.INFO,
                    "AccessibilityAspect >> aroundAspect >> {0} >> execute: {1} ms",
                    new Object[]{description, System.currentTimeMillis() - startTime}
            );
        }
    }
}
