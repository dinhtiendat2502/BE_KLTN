package com.app.toeic.service.impl;

import com.app.toeic.dto.LoginDto;
import com.app.toeic.dto.RegisterDto;
import com.app.toeic.enums.ERole;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.model.Role;
import com.app.toeic.model.UserAccount;
import com.app.toeic.repository.IRoleRepository;
import com.app.toeic.repository.IUserAccountRepository;
import com.app.toeic.response.ResponseVO;
import com.app.toeic.service.UserService;
import com.app.toeic.util.HttpStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = iUserRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> rolesNames = new ArrayList<>();
        user.getRoles().forEach(r -> rolesNames.add(r.getRoleName()));
        var token = jwtUtilities.generateToken(user.getUsername(), rolesNames);

        return new ResponseVO("Success", token, HttpStatus.OK);
    }

    @Override
    public ResponseVO register(RegisterDto registerDto) {
        if (iUserRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseVO("Email is already taken !", "Error", HttpStatus.SEE_OTHER);
        }
        if (iUserRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseVO("Username is already taken !", "Error", HttpStatus.SEE_OTHER);
        }

        UserAccount user = new UserAccount();
        user.setEmail(registerDto.getEmail());
        user.setFullName(registerDto.getFullName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role role = iRoleRepository.findByRoleName(ERole.USER);
        user.setRoles(Collections.singletonList(role));
        var userAccount = iUserRepository.save(user);
        return new ResponseVO("Success", userAccount, HttpStatus.CREATED);
    }

}
