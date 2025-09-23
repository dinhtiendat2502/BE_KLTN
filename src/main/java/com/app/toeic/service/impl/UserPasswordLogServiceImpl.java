package com.app.toeic.service.impl;

import com.app.toeic.model.UserAccountPasswordLog;
import com.app.toeic.repository.IUserAccountPasswordLogRepository;
import com.app.toeic.service.UserPasswordLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserPasswordLogServiceImpl implements UserPasswordLogService {
    private final IUserAccountPasswordLogRepository userAccountPasswordLogRepository;

    @Override
    public void savePasswordLog(UserAccountPasswordLog item) {
        userAccountPasswordLogRepository.save(item);
    }

    @Override
    public List<UserAccountPasswordLog> getAllPasswordLog() {
        return userAccountPasswordLogRepository.findAll();
    }
}
