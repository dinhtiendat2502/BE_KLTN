package com.app.toeic.userexam.service;

import com.app.toeic.userexam.model.UserAnswer;

import java.util.List;

public interface UserAnswerService {
    void saveAll(List<UserAnswer> list);
}
