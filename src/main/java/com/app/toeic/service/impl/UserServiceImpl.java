package com.app.toeic.service.impl;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.dto.UserDto;
import com.app.toeic.dto.UserUpdateDto;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final IUserAccountRepository iUserRepository;
    private final IRoleRepository iRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtUtilities;
    private final UserDetailsService userDetailsService;

    @Override
    public ResponseVO authenticate(LoginDto loginDto) {
        var v1 = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(v1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Không tìm thấy email!"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), rolesNames);
        return ResponseVO.builder().success(Boolean.TRUE).data(token).message("Đăng nhập thành công").build();
    }

    @Override
    public ResponseVO register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(iUserRepository.existsByEmail(registerDto.getEmail()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Email đã được đăng ký!");
        }
        var user = UserAccount.builder().email(registerDto.getEmail()).fullName(registerDto.getFullName()).password(passwordEncoder.encode(registerDto.getPassword())).avatar(AvatarHelper.getAvatar("")).roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER))).status(EUser.INACTIVE).build();

        iUserRepository.save(user);
        return ResponseVO.builder().success(Boolean.TRUE).data(null).message("Đăng kí tài khoản thành công").build();
    }

    @Override
    public ResponseVO getAllUser() {
        return ResponseVO.builder().success(Boolean.TRUE).data(iUserRepository.findAllByRolesNotContains(iRoleRepository.findByRoleName(ERole.ADMIN))).message("Lấy danh sách user thành công").build();
    }

    @Override
    public ResponseVO updateUser(UserDto user) {
        var u = iUserRepository.findById(user.getId()).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "User not found"));
        u.setStatus(user.getStatus());
        return ResponseVO.builder().success(Boolean.TRUE).data(iUserRepository.save(u)).message("Cập nhật user thành công").build();
    }

    @Override
    public UserAccount findByEmail(String email) {
        return iUserRepository.findByEmail(email).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Email này chưa đăng ký tài khoản!"));
    }

    @Override
    public ResponseVO updatePassword(String email, String newPassword) {
        var user = iUserRepository.findByEmail(email).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Email này chưa đăng ký tài khoản!"));
        user.setPassword(passwordEncoder.encode(newPassword));
        iUserRepository.save(user);
        return ResponseVO.builder().success(Boolean.TRUE).data(null).message("Cập nhật mật khẩu thành công!").build();
    }

    @Override
    public Object updateProfile(UserUpdateDto userUpdateDto, UserAccount user) {
        if (!passwordEncoder.matches(userUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.SEE_OTHER, "Mật khẩu hiện tại không đúng!");
        }
        if (StringUtils.isNotEmpty(userUpdateDto.getFullName()))
            user.setFullName(userUpdateDto.getFullName());
        user.setPassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
        iUserRepository.save(user);
        return "Cập nhật thông tin thành công!";
    }

    @Override
    public ResponseVO updateAvatar(UserAccount userAccount) {
        iUserRepository.save(userAccount);
        return ResponseVO.builder().success(Boolean.TRUE).data(null).message("Cập nhật avatar thành công!").build();
    }

    @Override
    public Optional<UserAccount> getProfile(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (Boolean.TRUE.equals(jwtUtilities.validateToken(token, userDetails))) {
                var user = iUserRepository.findByEmail(email).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Email này chưa đăng ký tài khoản!"));
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
