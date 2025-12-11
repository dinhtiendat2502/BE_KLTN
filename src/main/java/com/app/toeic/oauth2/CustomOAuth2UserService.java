package com.app.toeic.oauth2;

import com.app.toeic.email.service.impl.EmailServiceImpl;
import com.app.toeic.exception.AppException;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.oauth2.user.OAuth2UserInfo;
import com.app.toeic.oauth2.user.OAuth2UserInfoFactory;
import com.app.toeic.user.enums.ERole;
import com.app.toeic.user.enums.EUser;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.model.UserAccountLog;
import com.app.toeic.user.payload.LoginSocialDTO;
import com.app.toeic.user.repo.IRoleRepository;
import com.app.toeic.user.repo.IUserAccountLogRepository;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import com.app.toeic.util.ServerHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    IUserAccountRepository iUserAccountRepository;
    IUserAccountLogRepository iUserAccountLogRepository;
    PasswordEncoder passwordEncoder;
    IRoleRepository iRoleRepository;
    EmailServiceImpl emailService;
    SystemConfigService systemConfigService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        var oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration()
                                 .getRegistrationId(),
                oAuth2User.getAttributes()
        );
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "EMAIL_NOT_FOUND_FROM_OAUTH2_PROVIDER");
        }

        var userOptional = iUserAccountRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserAccount user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())) {
                throw new AppException(
                        HttpStatus.SEE_OTHER,
                        MessageFormat.format("EMAIL_EXISTED_WITH_OTHER_{0}", user.getProvider())
                );
            } else if (EUser.INACTIVE.equals(user.getStatus())) {
                throw new AppException(HttpStatus.SEE_OTHER, "ACCOUNT_NOT_ACTIVE");
            } else if (EUser.BLOCKED.equals(user.getStatus())) {
                throw new AppException(HttpStatus.SEE_OTHER, "ACCOUNT_BLOCKED");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
            var logUser = UserAccountLog.builder()
                                        .action(Constant.LOGIN_WITH_GOOGLE_FB)
                                        .description("Login with google or facebook")
                                        .lastIpAddress(ServerHelper.getClientIp())
                                        .userAccount(user)
                                        .build();
            iUserAccountLogRepository.save(logUser);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private UserAccount registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        var password = UUID.randomUUID().toString();
        var user = UserAccount
                .builder()
                .provider(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                .fullName(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .roles(Collections.singleton(iRoleRepository.findByRoleName(ERole.USER)))
                .avatar(oAuth2UserInfo.getImageUrl())
                .password(passwordEncoder.encode(password))
                .build();
        var returnUser = iUserAccountRepository.save(user);
        var linkFe = systemConfigService.getConfigValue(Constant.URL_FRONTEND);
        var loginSocialDTO = LoginSocialDTO
                .builder()
                .email(user.getEmail())
                .provider(user.getProvider())
                .fullName(user.getFullName())
                .url(linkFe)
                .build();
        emailService.sendEmailAccount(loginSocialDTO, password, Constant.LOGIN_SOCIAL);
        return returnUser;
    }

    private UserAccount updateExistingUser(UserAccount existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setAvatar(oAuth2UserInfo.getImageUrl());
        return iUserAccountRepository.save(existingUser);
    }
}
