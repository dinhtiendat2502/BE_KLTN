package com.app.toeic.service.impl;

import com.app.toeic.enums.EUser;
import com.app.toeic.repository.IUserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final IUserAccountRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = iUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email!"));
        return user;
    }
}
