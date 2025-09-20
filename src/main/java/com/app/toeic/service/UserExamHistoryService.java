package com.app.toeic.service;

import com.app.toeic.model.UserExamHistory;

import java.util.Optional;

public interface UserExamHistoryService {
    UserExamHistory save(UserExamHistory userExamHistory);

    Integer calculateScoreListening(Integer numberCorrectAnswer);

    Integer calculateScoreReading(Integer numberCorrectAnswer);

    Object findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);
}
