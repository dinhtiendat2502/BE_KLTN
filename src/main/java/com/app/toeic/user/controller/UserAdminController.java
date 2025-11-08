package com.app.toeic.user.controller;


import com.app.toeic.user.payload.UserDTO;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseVO getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping("/update-user")
    public ResponseVO updateUser(@RequestBody UserDTO user) {
        return userService.updateUser(user);
    }
}
