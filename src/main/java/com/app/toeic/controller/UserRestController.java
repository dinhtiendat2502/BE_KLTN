package com.app.toeic.controller;


import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseVO register(@RequestBody RegisterDto registerDto) {
        try {
            return userService.register(registerDto);
        } catch (Exception e) {
            return new ResponseVO(e.getMessage(), "", HttpStatus.SEE_OTHER);
        }
    }

    @PostMapping("/authenticate")
    public ResponseVO authenticate(@RequestBody LoginDto loginDto) {
        try {
            return userService.authenticate(loginDto);
        } catch (Exception e) {
            return new ResponseVO(e.getMessage(), "", HttpStatus.SEE_OTHER);
        }
    }
}
