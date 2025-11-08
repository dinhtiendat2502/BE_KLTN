package com.app.toeic.aop.aspect;

import com.app.toeic.aop.annotation.ActivityLog;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.controller.UserController;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.model.UserAccountLog;
import com.app.toeic.user.repo.IUserAccountLogRepository;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.JsonConverter;
import com.app.toeic.util.ServerHelper;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;

@Log
@Aspect
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogAspect {
    UserService userService;
    IUserAccountLogRepository iUserAccountLogRepository;

    @Around(value = "@annotation(activityLog)", argNames = "pjp, activityLog")
    public Object aroundAspect(final ProceedingJoinPoint pjp, final ActivityLog activityLog) throws Throwable {
        var startTime = System.currentTimeMillis();
        var params = pjp.getArgs();
        var result = ObjectUtils.CONST(null);
        var currentUser = userService.getCurrentUser();
        try {
            result = pjp.proceed();
            return result;
        } catch (Exception e) {
            log.log(Level.WARNING, "AccessibilityAspect >> aroundAspect >> Exception: {0}", e);
            throw e;
        } finally {
            var userAccountLog = getUserAccountLog(
                    params,
                    activityLog.activity(),
                    activityLog.description(),
                    result,
                    currentUser
            );
            iUserAccountLogRepository.save(userAccountLog);
            log.info(MessageFormat.format(
                    "ActivityLogAspect >> aroundAspect >> Finally >> action: {0} >> time: {1} ms",
                    activityLog.activity(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private UserAccountLog getUserAccountLog(
            Object[] params,
            String action,
            String description,
            Object result,
            Optional<UserAccount> currentUser
    ) {
        var ip = ServerHelper.getClientIp();
        var des = MessageFormat.format("{0} >> {1}", description, action);
        var rs = UserAccountLog
                .builder()
                .action(action)
                .lastIpAddress(ip);
        currentUser.ifPresent(rs::userAccount);
        var user = currentUser.orElse(null);
        currentUser.ifPresent(e -> rs.lastUpdatedBy(e.getFullName()));

        switch (action) {
            case Constant.FORGOT_PASSWORD -> {
                if (params[0] instanceof String str) {
                    rs.oldData(str);
                    rs.description(MessageFormat.format("{0} >> param: {1}", des, str));
                }
            }
            case Constant.UPDATE_PASSWORD -> rs.description(des);
            case Constant.RESET_PASSWORD -> {
                if (result instanceof ResponseVO vo && (vo.getData() instanceof Integer uid)) {
                    rs.userAccount(UserAccount.builder().userId(uid).build());

                }
            }
            case Constant.UPDATE_AVATAR -> {
                if (user != null) {
                    rs.oldData(user.getAvatar());
                    if (result instanceof ResponseVO vo && (vo.getData() instanceof String avatar)) {
                        rs.newData(avatar);
                    }
                }
                rs.description(des);
            }
            case Constant.UPDATE_PROFILE -> {
                if (user != null && (result instanceof ResponseVO vo)) {
                    if (vo.getData() instanceof UserController.NewInfoUser newData) {
                        rs.newData(JsonConverter.convertObjectToJson(newData));
                    }
                    rs.oldData(JsonConverter.convertObjectToJson(new Object[]{user.getFullName(), user.getPhone(), user.getAddress(), user.getAvatar()}));

                }
                var fileUpload = ObjectUtils.CONST(null);
                if (params[0] instanceof MultipartFile file) {
                    fileUpload = new Object[]{file.getOriginalFilename(), file.getSize(), file.getContentType()};
                }
                rs.description(MessageFormat.format(
                        "{0} >> param: {1}",
                        des,
                        JsonConverter.convertObjectToJson(new Object[]{params[1], params[2], params[3], fileUpload})
                ));
            }
            default -> log.info(STR."ActivityLogAspect >> getUserAccountLog >> action not found: \{action}");
        }
        return rs.build();
    }
}
