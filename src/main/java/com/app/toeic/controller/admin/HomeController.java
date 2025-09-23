package com.app.toeic.controller.admin;


import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

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
