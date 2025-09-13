package com.app.toeic.service;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.model.Role;
import com.app.toeic.model.UserAccount;
import com.app.toeic.response.ResponseVO;

public interface UserService {
    ResponseVO authenticate(LoginDto loginDto);
    ResponseVO register (RegisterDto registerDto);
}
