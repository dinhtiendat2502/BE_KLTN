package com.app.toeic.user.controller;


import com.app.toeic.external.payload.EmailDTO;
import com.app.toeic.user.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.email.service.EmailService;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.app.toeic.user.payload.*;
import com.app.toeic.user.repo.IOtpRepository;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    EmailService emailService;
    FirebaseStorageService firebaseStorageService;
    IOtpRepository otpRepository;

    private static final String NOT_FOUNT_USER = "NOT_FOUNT_USER";

    @PostMapping("/register")
    public ResponseVO register(@Valid @RequestBody RegisterDTO registerDto) {
        return userService.register(registerDto);
    }

    @PostMapping("/login-social")
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
    public ResponseVO authenticate(@Valid @RequestBody LoginDTO loginDto) {
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

    @PostMapping("/forgot-password/{email}")
    public Object forgotPassword(@PathVariable("email") String email) {
        return userService.forgotPassword(email);

    }

    @PatchMapping("/update-password")
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
        if(file != null && !file.isEmpty()) {
            var avatar = firebaseStorageService.uploadFile(file);
            if (StringUtils.isNotBlank(avatar)) {
                profile.setAvatar(avatar);
            }
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(userService.updateProfile(profile))
                .message("UPDATE_PROFILE_SUCCESS")
                .build();
    }

    @PostMapping("/update-avatar")
    public ResponseVO updateAvatar(
            @Valid @RequestBody UserUpdateAvatarDTO updateAvatarDto,
            HttpServletRequest request
    ) {
        var profile = userService
                .getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        profile.setAvatar(updateAvatarDto.getAvatar());
        return userService.updateAvatar(profile);
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
        final String[] msg = new String[1];
        AtomicBoolean success = new AtomicBoolean(false);
        var otp = otpRepository.findByEmailAndAction(payload.email, "REGISTER");
        otp.ifPresentOrElse(e -> {
            // check if otp was sent over 5 minutes
            var otpCreationTime = e.getCreatedAt();
            var currentTime = LocalDateTime.now();
            var minutesElapsed = Duration.between(otpCreationTime, currentTime).toMinutes();
            var isOtpExpired = minutesElapsed > 5;
            if(isOtpExpired) {
                msg[0] = "OTP_EXPIRED";
            } else {
                var otpCorrect = e.getOtpCode().equalsIgnoreCase(payload.otp);
                if(otpCorrect) {
                    var user = userService.findByEmail(payload.email);
                    user.setStatus(EUser.ACTIVE);
                    userService.save(user);
                    msg[0] = "CONFIRM_OTP_SUCCESS";
                    success.set(true);
                }else {
                    msg[0] = "OTP_INCORRECT";
                }
            }
            otpRepository.delete(e);
        }, () -> msg[0] = "OTP_INCORRECT");

        return ResponseVO
                .builder()
                .success(success.get())
                .data(null)
                .message(msg[0])
                .build();
    }

    @GetMapping("/test")
    public ResponseVO test() {
        return new ResponseVO(Boolean.TRUE, "test", "Get assets successfully");
    }

    public record ConfirmEmail(String email, String otp) {
    }
}
