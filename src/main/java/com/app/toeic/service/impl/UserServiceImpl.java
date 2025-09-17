package com.app.toeic.service.impl;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.dto.UserDto;
import com.app.toeic.enums.ERole;
import com.app.toeic.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.model.UserAccount;
import com.app.toeic.repository.IRoleRepository;
import com.app.toeic.repository.IUserAccountRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserService;
import com.app.toeic.util.AvatarHelper;
import com.app.toeic.util.HttpStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final IUserAccountRepository iUserRepository;
    private final IRoleRepository iRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtilities;

    @Override
    public ResponseVO authenticate(LoginDto loginDto) {
        var v1 = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword());
        Authentication authentication = authenticationManager
                .authenticate(v1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = iUserRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Không tìm thấy email!"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), rolesNames);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(token)
                .message("Đăng nhập thành công")
                .build();
    }

    @Override
    public ResponseVO register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(iUserRepository.existsByEmail(registerDto.getEmail()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Email đã được đăng ký!");
        }
        var user = UserAccount
                .builder()
                .email(registerDto.getEmail())
                .fullName(registerDto.getFullName())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .avatar(AvatarHelper.getAvatar(""))
                .roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER)))
                .status(EUser.INACTIVE)
                .build();

        iUserRepository.save(user);

        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(null)
                .message("Đăng kí tài khoản thành công")
                .build();
    }

    @Override
    public ResponseVO getAllUser() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iUserRepository.findAll())
                .message("Lấy danh sách user thành công")
                .build();
    }

    @Override
    public ResponseVO updateUser(UserDto user) {
        var u = iUserRepository.findById(user.getId()).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "User not found"));
        u.setStatus(user.getStatus());
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(iUserRepository.save(u))
                .message("Cập nhật user thành công")
                .build();
    }
}
