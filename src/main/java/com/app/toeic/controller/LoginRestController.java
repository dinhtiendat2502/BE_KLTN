package com.app.toeic.controller;


import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.impl.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class LoginRestController {
    private final CustomerUserDetailsService customerUserDetailsService;

}
