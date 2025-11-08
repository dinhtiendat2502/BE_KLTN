package com.app.toeic.user.response;

import com.app.toeic.user.enums.EUser;
import com.app.toeic.user.enums.UType;


public interface UserAccountRepsonse {
    Integer getUser_id();

    String getFull_name();

    String getPhone();

    String getAddress();

    String getEmail();

    String getAvatar();

    EUser getStatus();

    String getProvider();

    UType getUserType();
}
