package com.app.toeic.external.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class HomeController {
    UserService userService;

    @GetMapping("/is-login")
    public ResponseVO isLogin(HttpServletRequest request) {
        var isLogin = userService.isAdminLogin(request);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(isLogin)
                .build();
    }
}
