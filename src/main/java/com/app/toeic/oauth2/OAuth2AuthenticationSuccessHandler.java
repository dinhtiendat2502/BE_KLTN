package com.app.toeic.oauth2;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.jwt.JwtTokenProvider;
import com.app.toeic.user.model.Role;
import com.app.toeic.user.model.UserToken;
import com.app.toeic.user.repo.IUserAccountRepository;
import com.app.toeic.user.repo.UserTokenRepository;
import com.app.toeic.util.Constant;
import com.app.toeic.util.CookieUtils;
import com.app.toeic.util.HttpStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.logging.Level;

@Log
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    JwtTokenProvider tokenProvider;
    IUserAccountRepository iUserAccountRepository;
    SystemConfigService systemConfigService;
    UserTokenRepository userTokenRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.log(Level.FINE, "Response has already been committed. Unable to redirect to {0}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        var redirectUri = CookieUtils.get(request, Constant.REDIRECT_URI_PARAM_COOKIE_NAME)
                                     .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "CAN_NOT_PROCEED_WITH_AUTHENTICATION");
        }

        var targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        var user = iUserAccountRepository.findByEmail(authentication.getName())
                                         .orElseThrow(() -> new AppException(
                                                 HttpStatus.UNAUTHORIZED,
                                                 "USER_NOT_FOUND"
                                         ));
        var token = tokenProvider.generateTokenV2(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(Role::getRoleName).toList()
        );

        var userTokenOptional = userTokenRepository.findByEmail(user.getEmail());
        userTokenOptional.ifPresentOrElse(uToken -> {
            uToken.setToken(token.getToken());
            uToken.setCreatedDate(token.getCreatedDate());
            uToken.setExpiredDate(token.getExpiredDate());
            userTokenRepository.save(uToken);
        }, () -> {
            var userToken = UserToken
                    .builder()
                    .token(token.getToken())
                    .email(user.getEmail())
                    .createdDate(token.getCreatedDate())
                    .expiredDate(token.getExpiredDate())
                    .build();
            userTokenRepository.save(userToken);
        });

        return UriComponentsBuilder.fromUriString(targetUrl)
                                   .queryParam("token", token.getToken())
                                   .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        var clientRedirectUri = URI.create(uri);
        var systemRedirectUri = systemConfigService.getConfigValue(Constant.AUTHORIZED_REDIRECT_URI);
        if (StringUtils.isBlank(systemRedirectUri)) {
            return true;
        }
        var listRedirectUri = systemRedirectUri.split(",");
        return Arrays.stream(listRedirectUri)
                     .anyMatch(authorizedRedirectUri -> {
                         var authorizedUri = URI.create(authorizedRedirectUri);
                         return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                 && authorizedUri.getPort() == clientRedirectUri.getPort();
                     });
    }
}
