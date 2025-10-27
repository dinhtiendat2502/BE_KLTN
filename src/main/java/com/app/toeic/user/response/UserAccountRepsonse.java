package com.app.toeic.user.response;

import com.app.toeic.user.enums.EUser;


public interface UserAccountRepsonse {
    Integer getUser_id();

    String getFull_name();
    String getPhone();

    String getAddress();

    String getEmail();

    String getAvatar();

    EUser getStatus();
    String getProvider();
}
