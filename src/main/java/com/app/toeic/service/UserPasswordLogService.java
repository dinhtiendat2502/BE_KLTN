package com.app.toeic.service;

import com.app.toeic.model.UserAccountPasswordLog;

import java.util.List;

public interface UserPasswordLogService {
    void savePasswordLog(UserAccountPasswordLog item);

    List<UserAccountPasswordLog> getAllPasswordLog();
}
