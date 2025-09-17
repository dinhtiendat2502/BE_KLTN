package com.app.toeic.controller;


import com.app.toeic.dto.EmailDto;
import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
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
}
