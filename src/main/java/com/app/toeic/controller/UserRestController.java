package com.app.toeic.controller;


import com.app.toeic.dto.*;
import com.app.toeic.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.EmailService;
import com.app.toeic.service.FirebaseStorageService;
import com.app.toeic.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final EmailService emailService;

    private final String NOT_FOUNT_USER = "Không tìm thấy thông tin người dùng";

    @PostMapping("/register")
    public ResponseVO register(@Valid @RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @PostMapping("/authenticate")
    public ResponseVO authenticate(@Valid @RequestBody LoginDto loginDto) {
        return userService.authenticate(loginDto);
    }

    @PostMapping("/send-email")
    public ResponseVO sendEmail(@Valid @RequestBody EmailDto emailDto) {
        return emailService.sendEmail(emailDto, "email-template");
    }

    @PatchMapping("/update-password")
    public ResponseVO updatePassword(@Valid @RequestBody UserUpdatePasswordDto userUpdatePasswordDto, HttpServletRequest request) {
        var profile = userService.getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        if (StringUtils.compareIgnoreCase(userUpdatePasswordDto.getNewPassword(), userUpdatePasswordDto.getConfirmPassword()) != 0) {
            return ResponseVO.builder().success(Boolean.FALSE).message("Mật khẩu không khớp").build();
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(userService.updatePassword(userUpdatePasswordDto, profile))
                .message("Cập nhật mật khẩu thành công")
                .build();
    }

    @PatchMapping("/update-profile")
    public ResponseVO updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        var profile = userService.getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        profile.setFullName(userUpdateDto.getFullName());
        profile.setPhone(userUpdateDto.getPhone());
        profile.setAddress(userUpdateDto.getAddress());
        userService.save(profile);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("Cập nhật thông tin thành công")
                .build();
    }

    @PostMapping("/update-avatar")
    public ResponseVO updateAvatar(@Valid @RequestBody UserUpdateAvatarDto updateAvatarDto, HttpServletRequest request) throws IOException {
        var profile = userService.getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        profile.setAvatar(updateAvatarDto.getAvatar());
        return userService.updateAvatar(profile);
    }

    @GetMapping("/get-profile")
    public ResponseVO getProfile(HttpServletRequest request) {
        var profile = userService.getProfile(request)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, NOT_FOUNT_USER));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(profile)
                .message("Get profile successfully")
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
    public ResponseVO confirmOtp(String email) {
        var user = userService.findByEmail(email);
        user.setStatus(EUser.ACTIVE);
        userService.save(user);
        return ResponseVO.builder().success(Boolean.TRUE).data(null).message("Xác thực tài khoản thành công!").build();
    }

    @GetMapping("/test")
    public ResponseVO test() {
        return new ResponseVO(Boolean.TRUE, "test", "Get assets successfully");
    }
}
