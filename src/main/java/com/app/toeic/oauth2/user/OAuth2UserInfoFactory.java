package com.app.toeic.oauth2.user;

import com.app.toeic.exception.AppException;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(Constant.GOOGLE.equalsIgnoreCase(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (Constant.FACEBOOK.equalsIgnoreCase(registrationId)) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new AppException(HttpStatus.UNAUTHORIZED, "NOT_SUPPORTED");
        }
    }
}
