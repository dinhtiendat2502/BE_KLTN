package com.app.toeic.service;

import com.app.toeic.dto.*;
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

    Object updatePassword(UserUpdatePasswordDto userUpdateDto, UserAccount user);

    ResponseVO updateAvatar(UserAccount userAccount);

    Optional<UserAccount> getProfile(HttpServletRequest request);

    Boolean isLogin(HttpServletRequest request);

    void save(UserAccount userAccount);

    Boolean keepAlive(HttpServletRequest request);

    Object updateProfile(UserAccount profile);

    Object loginSocial(LoginSocialDto loginSocialDto);

    Object isAdminLogin(HttpServletRequest request);
}
