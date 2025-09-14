package com.app.toeic.service;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.dto.UserDto;
import com.app.toeic.response.ResponseVO;

public interface UserService {
    ResponseVO authenticate(LoginDto loginDto);
    ResponseVO register (RegisterDto registerDto);

    ResponseVO getAllUser();

    ResponseVO updateUser(UserDto user);
}
