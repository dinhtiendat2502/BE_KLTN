package com.app.toeic.user.service;

import com.app.toeic.user.model.UserToken;
import com.app.toeic.user.repo.UserTokenRepository;
import com.app.toeic.user.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserTokenService {
    UserTokenRepository userTokenRepository;

    public void saveUserToken(LoginResponse token) {
        var userTokenOptional = userTokenRepository.findByEmail(token.getEmail());
        userTokenOptional.ifPresentOrElse(uToken -> {
            uToken.setToken(token.getToken());
            uToken.setCreatedDate(token.getCreatedDate());
            uToken.setExpiredDate(token.getExpiredDate());
            userTokenRepository.save(uToken);
        }, () -> {
            var userToken = UserToken
                    .builder()
                    .token(token.getToken())
                    .email(token.getEmail())
                    .createdDate(token.getCreatedDate())
                    .expiredDate(token.getExpiredDate())
                    .build();
            userTokenRepository.save(userToken);
        });
    }
}
