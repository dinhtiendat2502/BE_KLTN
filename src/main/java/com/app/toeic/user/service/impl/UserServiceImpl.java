package com.app.toeic.user.service.impl;

import com.app.toeic.email.service.impl.EmailServiceImpl;
import com.app.toeic.user.enums.ERole;
import com.app.toeic.user.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.payload.*;
import com.app.toeic.user.repo.IRoleRepository;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.user.response.LoginResponse;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.AvatarHelper;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    AuthenticationManager authenticationManager;
    IUserAccountRepository iUserRepository;
    IRoleRepository iRoleRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtUtilities;
    UserDetailsService userDetailsService;
    EmailServiceImpl emailService;

    private static final String EMAIL_NOT_REGISTERED = "EMAIL_NOT_REGISTERED";

    @Override
    public ResponseVO authenticate(LoginDTO loginDto) {
        var v1 = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(v1);
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        UserAccount user = iUserRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, EMAIL_NOT_REGISTERED));
        if (user
                .getStatus()
                .equals(EUser.INACTIVE)) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .data(user.getStatus())
                    .message("ACCOUNT_NOT_ACTIVE")
                    .build();
        } else if (user
                .getStatus()
                .equals(EUser.BLOCKED)) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("ACCOUNT_BLOCKED")
                    .build();
        }

        List<String> rolesNames = new ArrayList<>();
        user
                .getRoles()
                .forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), user.getPassword(), rolesNames);
        var res = LoginResponse
                .builder()
                .token(token)
                .email(user.getEmail())
                .roles(rolesNames)
                .build();
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(res)
                .message("LOGIN_SUCCESS")
                .build();
    }

    @Override
    public ResponseVO register(RegisterDTO registerDto) {
        if (Boolean.TRUE.equals(iUserRepository.existsByEmail(registerDto.getEmail()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "EMAIL_EXISTED");
        }
        emailService.sendEmail(registerDto.getEmail(), "AUTHENTICATION_AFTER_REGISTER");
        var user = UserAccount
                .builder()
                .email(registerDto.getEmail())
                .fullName(registerDto.getFullName())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .avatar(AvatarHelper.getAvatar(""))
                .roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER)))
                .status(EUser.INACTIVE)
                .provider("TOEICUTE")
                .build();
        iUserRepository.save(user);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("REGISTER_SUCCESS")
                .build();
    }

    @Override
    public ResponseVO getAllUser() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iUserRepository.findAllByRolesNotContains(iRoleRepository.findByRoleName(ERole.ADMIN)))
                .message("GET_ALL_USER_SUCCESS")
                .build();
    }

    @Override
    public ResponseVO updateUser(UserDTO user) {
        var u = iUserRepository
                .findById(user.getId())
                .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "USER_NOT_FOUND"));
        u.setStatus(user.getStatus());
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iUserRepository.save(u))
                .message("UPDATE_USER_SUCCESS")
                .build();
    }

    @Override
    public UserAccount findByEmail(String email) {
        return iUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, EMAIL_NOT_REGISTERED));
    }

    @Override
    public ResponseVO updatePassword(String email, String newPassword) {
        var user = iUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, EMAIL_NOT_REGISTERED));
        user.setPassword(passwordEncoder.encode(newPassword));
        iUserRepository.save(user);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("UPDATE_PASSWORD_SUCCESS")
                .build();
    }

    @Override
    public Object updatePassword(UserUpdatePasswordDTO userUpdateDto, UserAccount user) {
        if (!passwordEncoder.matches(userUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.SEE_OTHER, "CURRENT_PASSWORD_NOT_MATCH");
        }
        user.setPassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
        iUserRepository.save(user);
        return "Cập nhật thông tin thành công!";
    }

    @Override
    public ResponseVO updateAvatar(UserAccount userAccount) {
        iUserRepository.save(userAccount);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("UPDATE_AVATAR_SUCCESS")
                .build();
    }

    @Override
    public Optional<UserAccount> getProfile(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            var userDetails = userDetailsService.loadUserByUsername(email);
            if (Boolean.TRUE.equals(jwtUtilities.validateToken(token, userDetails))) {
                var user = iUserRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, EMAIL_NOT_REGISTERED));
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Boolean isLogin(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            var userDetails = userDetailsService.loadUserByUsername(email);
            return jwtUtilities.validateToken(token, userDetails);
        }
        return Boolean.FALSE;
    }

    @Override
    public void save(UserAccount userAccount) {
        iUserRepository.save(userAccount);
    }

    @Override
    public Boolean keepAlive(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Object updateProfile(UserAccount profile) {
        return iUserRepository.save(profile);
    }

    @Override
    public Object loginSocial(LoginSocialDTO loginSocialDto) {
        var user = iUserRepository.findByEmail(loginSocialDto.getEmail());
        var tokens = new ArrayList<String>();
        user.ifPresentOrElse(u -> {
            if (u.getStatus()
                    .equals(EUser.INACTIVE)) {
                throw new AppException(HttpStatus.SEE_OTHER, "ACCOUNT_NOT_ACTIVE");
            } else if (u.getStatus()
                    .equals(EUser.BLOCKED)) {
                throw new AppException(HttpStatus.SEE_OTHER, "ACCOUNT_BLOCKED");
            } else if (!u.getProvider()
                    .equals(loginSocialDto.getProvider())) {
                throw new AppException(
                        HttpStatus.SEE_OTHER,
                        "EMAIL_EXISTED_WITH_OTHER_PROVIDER"
                );
            }
            var rolesNames = new ArrayList<String>();
            u.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
            final var token = jwtUtilities.generateToken(u.getUsername(), u.getPassword(), rolesNames);
            tokens.add(token);
        }, () -> {
            var password = randomPassword();
            var newUser = UserAccount
                    .builder()
                    .email(loginSocialDto.getEmail())
                    .fullName(loginSocialDto.getFullName())
                    .avatar(loginSocialDto.getAvatar())
                    .roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER)))
                    .status(EUser.ACTIVE)
                    .provider(loginSocialDto.getProvider())
                    .password(passwordEncoder.encode(password))
                    .build();
            iUserRepository.save(newUser);
            List<String> rolesNames = new ArrayList<>();
            newUser.getRoles()
                    .forEach(r -> rolesNames.add(r.getRoleName()));
            final var token = jwtUtilities.generateToken(newUser.getUsername(), newUser.getPassword(), rolesNames);
            tokens.add(token);
            emailService.sendEmailAccount(loginSocialDto, password, "LOGIN_SOCIAL");
        });
        return tokens.getFirst();
    }

    @Override
    public Object isAdminLogin(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var listRoles = jwtUtilities.extractRoles(token);

            if (Boolean.TRUE.equals(
                    jwtUtilities.validateToken(token, userDetails))
                    && listRoles
                    .stream()
                    .map(String::valueOf)
                    .anyMatch(r -> r.contains(ERole.ADMIN.name()))) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public String forgotPassword(String email) {
        emailService.sendEmail(email, "FORGOT_PASSWORD");
        return "SEND_EMAIL_SUCCESS";
    }

    public String randomPassword() {
        return UUID
                .randomUUID()
                .toString();
    }
}
