package com.app.toeic.controller.admin;


import com.app.toeic.dto.UserDto;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseVO getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/update-user")
    public ResponseVO updateUser(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }
}
