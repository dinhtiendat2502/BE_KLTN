package com.app.toeic.service.impl;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.enums.ERole;
import com.app.toeic.enums.EUser;
import com.app.toeic.exception.AppException;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.model.Role;
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = iUserRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(HttpStatus.SEE_OTHER, "User not found"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), rolesNames);

        return new ResponseVO(Boolean.TRUE, token, "Login successfully");
    }

    @Override
    public ResponseVO register(RegisterDto registerDto) {
        if (iUserRepository.existsByEmail(registerDto.getEmail())) {
            throw new AppException(HttpStatus.SEE_OTHER, "Email is already taken !");
        }
        if (iUserRepository.existsByUsername(registerDto.getUsername())) {
            throw new AppException(HttpStatus.SEE_OTHER, "Username is already taken !");
        }

        UserAccount user = new UserAccount();
        user.setEmail(registerDto.getEmail());
        user.setFullName(registerDto.getFullName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAddress(registerDto.getAddress());
        user.setPhone(registerDto.getPhone());
        user.setAvatar(AvatarHelper.getAvatar(user.getAvatar()));
        Role role = iRoleRepository.findByRoleName(ERole.USER);
        user.setRoles(Collections.singleton(role));
        user.setStatus(EUser.ACTIVE);
        var userAccount = iUserRepository.save(user);
        return new ResponseVO(Boolean.TRUE, userAccount, "Register successfully");
    }
}
