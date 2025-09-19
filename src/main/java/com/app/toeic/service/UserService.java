package com.app.toeic.service;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.dto.UserDto;
import com.app.toeic.model.UserAccount;
import com.app.toeic.response.ResponseVO;

public interface UserService {
    ResponseVO authenticate(LoginDto loginDto);

    ResponseVO register(RegisterDto registerDto);

    ResponseVO getAllUser();

    ResponseVO updateUser(UserDto user);

    UserAccount findByEmail(String email);

    ResponseVO updatePassword(String email, String newPassword);

    ResponseVO updateProfile(String email, String fullName, String password);

    ResponseVO updateAvatar(String email, String avatar);
}
