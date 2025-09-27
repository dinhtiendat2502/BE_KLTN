package com.app.toeic.user.service.impl;

import com.app.toeic.user.repo.IUserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomerUserDetailsService implements UserDetailsService {

    private final IUserAccountRepository iUserRepository;

    public CustomerUserDetailsService(IUserAccountRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return iUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email!"));
    }
}
