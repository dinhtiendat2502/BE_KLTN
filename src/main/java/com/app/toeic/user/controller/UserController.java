package com.app.toeic.user.controller;


import com.app.toeic.aop.annotation.ActivityLog;
import com.app.toeic.aop.annotation.AuthenticationLog;
import com.app.toeic.external.payload.EmailDTO;
import com.app.toeic.user.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.email.service.EmailService;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.user.payload.*;
import com.app.toeic.user.repo.IOtpRepository;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    EmailService emailService;
    FirebaseStorageService firebaseStorageService;
    IOtpRepository otpRepository;
    private static final int OTP_EXPIRED = 48;
    private static final String NOT_FOUNT_USER = "NOT_FOUNT_USER";
    PasswordEncoder passwordEncoder;


    @GetMapping(value = "get-captcha", produces = MediaType.IMAGE_JPEG_VALUE)
    public Object getCaptcha(HttpServletResponse response) {
        var captchaProperty = CaptchaGenerator.getCaptchaProperty();
        var minutes = 60 * 5;
        CookieUtils.add(response, Constant.CAPTCHA, AESUtils.encrypt(captchaProperty.answer()), minutes);
        return captchaProperty.captcha();
    }

    @PostMapping("/register")
    public ResponseVO register(@Valid @RequestBody RegisterDTO registerDto) {
        return userService.register(registerDto);
    }

    @PostMapping("/login-social")
    @AuthenticationLog(activity = Constant.LOGIN_WITH_GOOGLE_FB, description = "Login with google or facebook")
    public ResponseVO loginSocial(@Valid @RequestBody LoginSocialDTO loginSocialDto) {
        var token = userService.loginSocial(loginSocialDto);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(token)
                .message("LOGIN_SUCCESS")
                .build();
    }

    @PostMapping("/authenticate")
    @AuthenticationLog(activity = Constant.LOGIN, description = "Login with email and password")
    public ResponseVO authenticate(@Valid @RequestBody LoginDTO loginDto, HttpServletRequest request) {
        if (!userService.isValidCaptcha(request, loginDto.getCaptcha())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "CAPTCHA_INCORRECT");
        }
        return userService.authenticate(loginDto);
    }

    @PostMapping("/send-email")
    public ResponseVO sendEmail(@Valid @RequestBody EmailDTO emailDto) {
        emailService.sendEmail(emailDto.getTo(), emailDto.getTemplateCode());
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("SEND_EMAIL_SUCCESS")
                .build();
    }

    @PostMapping("/send-email-social")
    public Object sendEmailSocial(@Valid @RequestBody LoginSocialDTO emailDto) {
        return userService.loginSocial(emailDto);
    }

    @PostMapping("/forgot-password")
    @AuthenticationLog(activity = Constant.FORGOT_PASSWORD, description = "Forgot password")
    public Object forgotPassword(@RequestBody String email) {
        userService.findByEmail(email);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(userService.forgotPassword(email))
                .build();
    }

    @PatchMapping("/update-password")
    @ActivityLog(activity = Constant.UPDATE_PASSWORD, description = "Update password")
    public ResponseVO updatePassword(
            @Valid @RequestBody UserUpdatePasswordDTO userUpdatePasswordDto,
            HttpServletRequest request
    ) {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        if (StringUtils.compareIgnoreCase(
                userUpdatePasswordDto.getNewPassword(),
                userUpdatePasswordDto.getConfirmPassword()
        ) != 0) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("PASSWORD_NOT_MATCH")
                    .build();
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(userService.updatePassword(userUpdatePasswordDto, profile))
                .message("UPDATE_PASSWORD_SUCCESS")
                .build();
    }

    @PatchMapping(value = "/update-profile", consumes = {"multipart/form-data"})
    @ActivityLog(activity = Constant.UPDATE_PROFILE, description = "Update profile")
    public ResponseVO updateProfile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            HttpServletRequest request
    ) throws IOException {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        profile.setFullName(StringUtils.defaultIfBlank(fullName, profile.getFullName()));
        profile.setPhone(StringUtils.defaultIfBlank(phone, profile.getPhone()));
        profile.setAddress(StringUtils.defaultIfBlank(address, profile.getAddress()));
        if (file != null && !file.isEmpty()) {
            var avatar = firebaseStorageService.uploadFile(file);
            if (StringUtils.isNotBlank(avatar)) {
                profile.setAvatar(avatar);
            }
        }
        userService.updateProfile(profile);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(new NewInfoUser(profile.getFullName(), profile.getPhone(), profile.getAddress(), profile.getAvatar()))
                .message("UPDATE_PROFILE_SUCCESS")
                .build();
    }

    @PostMapping(value = "/update-avatar", consumes = {"multipart/form-data"})
    @ActivityLog(activity = Constant.UPDATE_AVATAR, description = "Update avatar")
    public ResponseVO updateAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("FILE_NOT_FOUND")
                    .build();
        }
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        var avatar = firebaseStorageService.uploadFile(file);
        profile.setAvatar(StringUtils.defaultIfBlank(avatar, profile.getAvatar()));
        userService.updateAvatar(profile);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(avatar)
                .message("UPDATE_AVATAR_SUCCESS")
                .build();
    }

    @Operation(security = {@SecurityRequirement(name = "openApiSecurityScheme")})
    @GetMapping("/get-profile")
    public ResponseVO getProfile(HttpServletRequest request) {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(profile)
                .message("GET_PROFILE_SUCCESS")
                .build();
    }

    @GetMapping("/is-login")
    public ResponseVO isLogin(HttpServletRequest request) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(userService.isLogin(request))
                .build();
    }

    @GetMapping("/keep-alive")
    public ResponseVO keepAlive(HttpServletRequest request) {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(userService.keepAlive(request))
                .build();
    }

    @PostMapping("/confirm-otp")
    public ResponseVO confirmOtp(@RequestBody ConfirmEmail payload) {
        final var msg = new String[1];
        var success = new AtomicBoolean(false);
        var otp = otpRepository.findByEmailAndAction(payload.email, Constant.REGISTER);
        otp.ifPresentOrElse(e -> {
            // check if otp was sent over 5 minutes
            var otpCreationTime = e.getCreatedAt();
            var currentTime = LocalDateTime.now();
            var minutesElapsed = Duration.between(otpCreationTime, currentTime).toMinutes();
            var timeExpired = 5;
            var isOtpExpired = minutesElapsed > timeExpired;
            if (isOtpExpired) {
                msg[0] = "OTP_EXPIRED";
            } else {
                var otpCorrect = e.getOtpCode().equalsIgnoreCase(payload.otp);
                if (otpCorrect) {
                    var user = userService.findByEmail(payload.email);
                    user.setStatus(EUser.ACTIVE);
                    userService.save(user);
                    msg[0] = "CONFIRM_OTP_SUCCESS";
                    success.set(true);
                } else {
                    msg[0] = Constant.OTP_INCORRECT;
                }
            }
            otpRepository.delete(e);
        }, () -> msg[0] = Constant.OTP_INCORRECT);

        return ResponseVO
                .builder()
                .success(success.get())
                .data(null)
                .message(msg[0])
                .build();
    }

    @PostMapping("reset-password")
    @ActivityLog(activity = Constant.RESET_PASSWORD, description = "Reset password")
    public Object resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        var otp = otpRepository.findByOtpCodeAndAction(resetPasswordDTO.otp, Constant.FORGOT_PASSWORD);
        final var msg = new String[1];
        final var success = new AtomicBoolean(false);
        var obj = new AtomicInteger();
        otp.ifPresentOrElse(e -> {
            var otpCreationTime = e.getCreatedAt();
            var currentTime = LocalDateTime.now();
            var minutesElapsed = Duration.between(otpCreationTime, currentTime).toHours();
            var isOtpExpired = minutesElapsed > OTP_EXPIRED;
            var user = userService.findByEmail(e.getEmail());
            obj.set(user.getUserId());
            if (isOtpExpired) {
                msg[0] = "OTP_EXPIRED";
            } else {
                user.setPassword(passwordEncoder.encode(resetPasswordDTO.password));
                userService.save(user);
                msg[0] = "RESET_PASSWORD_SUCCESS";
                success.set(true);
            }
            otpRepository.delete(e);
        }, () -> msg[0] = Constant.OTP_INCORRECT);
        return ResponseVO
                .builder()
                .success(success.get())
                .message(msg[0])
                .data(obj.get())
                .build();
    }

    @Operation(security = {@SecurityRequirement(name = "openApiSecurityScheme")})
    @GetMapping("/activity")
    public Object userActivity(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "type", defaultValue = "ALL") String type
    ) {
        var rs = userService.getActivities(request, page, size, type);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(rs)
                .build();
    }

    @GetMapping("/test")
    @ActivityLog(activity = Constant.UPDATE_PASSWORD, description = "Update password")
    public ResponseVO test() {
        var ip = ServerHelper.getClientIp();
        log.log(Level.INFO, MessageFormat.format("Test log: {0}, ip: {1}", "Test", ip));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(Map.of("ip", ip, "test", "Test"))
                .message("Test log")
                .build();
    }

    public record ConfirmEmail(String email, String otp) {
    }

    public record ResetPasswordDTO(String otp, String password) {
    }
    public record NewInfoUser(String fullName, String phone, String address, String avatar) {
    }
}
