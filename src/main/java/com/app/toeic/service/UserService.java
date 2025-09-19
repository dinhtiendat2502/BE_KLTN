package com.app.toeic.service;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.dto.UserDto;
import com.app.toeic.dto.UserUpdateDto;
import com.app.toeic.model.UserAccount;
import com.app.toeic.response.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserService {
    ResponseVO authenticate(LoginDto loginDto);

    ResponseVO register(RegisterDto registerDto);

    ResponseVO getAllUser();

    ResponseVO updateUser(UserDto user);


    UserAccount findByEmail(String email);

    ResponseVO updatePassword(String email, String newPassword);

    Object updateProfile(UserUpdateDto userUpdateDto, UserAccount user);

    ResponseVO updateAvatar(UserAccount userAccount);

    Optional<UserAccount> getProfile(HttpServletRequest request);
}
