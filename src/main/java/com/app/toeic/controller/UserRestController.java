package com.app.toeic.controller;


import com.app.toeic.dto.*;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.EmailService;
import com.app.toeic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final EmailService emailService;

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
    public ResponseVO updatePassword(@Valid @RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        if (!userUpdatePasswordDto.getOldPassword().equals(userUpdatePasswordDto.getNewPassword())) {
            return ResponseVO.builder().success(Boolean.FALSE).message("Mật khẩu không khớp!").build();
        }
        return userService.updatePassword(userUpdatePasswordDto.getEmail(), userUpdatePasswordDto.getNewPassword());
    }

    @PatchMapping("/update-profile")
    public ResponseVO updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateProfile(userUpdateDto.getEmail(), userUpdateDto.getFullName(), userUpdateDto.getPassword());
    }

    @PatchMapping("/update-avatar")
    public ResponseVO updateAvatar(@Valid @RequestBody UserUpdateAvatarDto userUpdateAvatarDto) {
        return userService.updateAvatar(userUpdateAvatarDto.getEmail(), userUpdateAvatarDto.getAvatar());
    }
}
