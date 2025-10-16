package com.app.toeic.user.service;

import com.app.toeic.user.model.UserAccount;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.payload.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface UserService {
    ResponseVO authenticate(LoginDTO loginDto);

    ResponseVO register(RegisterDTO registerDto);

    ResponseVO getAllUser();

    ResponseVO updateUser(UserDTO user);


    UserAccount findByEmail(String email);

    ResponseVO updatePassword(String email, String newPassword);

    Object updatePassword(UserUpdatePasswordDTO userUpdateDto, UserAccount user);

    ResponseVO updateAvatar(UserAccount userAccount);

    Optional<UserAccount> getProfile(HttpServletRequest request);

    Boolean isLogin(HttpServletRequest request);

    void save(UserAccount userAccount);

    Boolean keepAlive(HttpServletRequest request);

    Object updateProfile(UserAccount profile);

    Object loginSocial(LoginSocialDTO loginSocialDto);

    Object isAdminLogin(HttpServletRequest request);

    String forgotPassword(String email);
}
