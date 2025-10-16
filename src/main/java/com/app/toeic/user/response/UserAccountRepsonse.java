package com.app.toeic.user.response;

import com.app.toeic.user.enums.EUser;

import java.util.Set;

public interface UserAccountRepsonse {
    Integer getUserId();

    String getFullName();
    String getPhone();

    String getAddress();

    String getEmail();

    String getAvatar();

    EUser getStatus();
    String getProvider();
}
