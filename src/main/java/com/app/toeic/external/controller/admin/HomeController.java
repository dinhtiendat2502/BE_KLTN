package com.app.toeic.external.controller.admin;


import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
