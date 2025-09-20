package com.app.toeic.service;

import com.app.toeic.model.UserAnswer;

import java.util.List;

public interface UserAnswerService {
    void saveAll(List<UserAnswer> list);
}
