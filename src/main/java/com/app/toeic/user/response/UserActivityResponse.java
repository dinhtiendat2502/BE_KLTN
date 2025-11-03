package com.app.toeic.user.response;

public interface UserActivityResponse {
    Long getUserAccountLogId();

    String getOldData();

    String getNewData();

    String getAction();

    String getLastIpAddress();

    interface UserActivity2Response extends UserActivityResponse {
        UserAccountInfoResponse getUserAccount();
    }

    interface UserAccountInfoResponse {
        String getEmail();
        String getFullName();
    }
}
