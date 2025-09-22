package com.app.toeic.service.impl;

import com.app.toeic.dto.*;
import com.app.toeic.enums.ERole;
import com.app.toeic.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.model.UserAccount;
import com.app.toeic.repository.IRoleRepository;
import com.app.toeic.repository.IUserAccountRepository;
import com.app.toeic.response.LoginResponse;
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

import java.util.*;

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
    private final EmailServiceImpl emailService;

    @Override
    public ResponseVO authenticate(LoginDto loginDto) {
        var v1 = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(v1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "Không tìm thấy email!"));
        if (user.getStatus().equals(EUser.INACTIVE)) {
            return ResponseVO.builder().success(Boolean.FALSE).data(user.getStatus()).message("Tài khoản chưa được kích hoạt!").build();
        } else if (user.getStatus().equals(EUser.BLOCKED)) {
            return ResponseVO.builder().success(Boolean.FALSE).message("Tài khoản đã bị khóa!").build();
        }

        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), user.getPassword(), rolesNames);
        var res = LoginResponse.builder().token(token).email(user.getEmail()).roles(rolesNames).build();
        return ResponseVO.builder().success(Boolean.TRUE).data(res).message("Đăng nhập thành công").build();
    }

    @Override
    public ResponseVO register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(iUserRepository.existsByEmail(registerDto.getEmail()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "Email đã được đăng ký!");
        }
        var user = UserAccount.builder().email(registerDto.getEmail()).fullName(registerDto.getFullName()).password(passwordEncoder.encode(registerDto.getPassword())).avatar(AvatarHelper.getAvatar("")).roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER))).status(EUser.INACTIVE).provider("TOEICUTE").build();

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
    public Object updatePassword(UserUpdatePasswordDto userUpdateDto, UserAccount user) {
        if (!passwordEncoder.matches(userUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.SEE_OTHER, "Mật khẩu hiện tại không đúng!");
        }
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

    @Override
    public Boolean isLogin(HttpServletRequest request) {
        var token = jwtUtilities.getToken(request);
        if (StringUtils.isNotEmpty(token) && jwtUtilities.validateToken(token)) {
            String email = jwtUtilities.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
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
    public Object loginSocial(LoginSocialDto loginSocialDto) {
        var user = iUserRepository.findByEmail(loginSocialDto.getEmail());
        List<String> tokens = new ArrayList<>();
        user.ifPresentOrElse(u -> {
            if (u.getStatus().equals(EUser.INACTIVE)) {
                throw new AppException(HttpStatus.SEE_OTHER, "Tài khoản chưa được kích hoạt!");
            } else if (u.getStatus().equals(EUser.BLOCKED)) {
                throw new AppException(HttpStatus.SEE_OTHER, "Tài khoản đã bị khóa!");
            }
            if (!u.getProvider().equals(loginSocialDto.getProvider())) {
                throw new AppException(HttpStatus.SEE_OTHER, "Tài khoản đã được đăng ký. Vui lòng đăng nhập bằng email và mật khẩu!");
            }
            List<String> rolesNames = new ArrayList<>();
            u.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
            final var token = jwtUtilities.generateToken(u.getUsername(), u.getPassword(), rolesNames);
            tokens.add(token);
        }, () -> {
            var password = randomPassword();
            var newUser = UserAccount.builder().email(loginSocialDto.getEmail()).fullName(loginSocialDto.getFullName()).avatar(loginSocialDto.getAvatar()).roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER))).status(EUser.ACTIVE).provider(loginSocialDto.getProvider()).password(passwordEncoder.encode(password)).build();
            iUserRepository.save(newUser);
            List<String> rolesNames = new ArrayList<>();
            newUser.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
            final var token = jwtUtilities.generateToken(newUser.getUsername(), newUser.getPassword(), rolesNames);
            tokens.add(token);
            emailService.sendEmailAccount(loginSocialDto, password, "email-account");
        });
        return tokens.get(0);
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

    public String randomPassword() {
        return UUID.randomUUID().toString();
    }
}
