package com.app.toeic.service;

import com.app.toeic.model.UserAccount;
import com.app.toeic.model.UserExamHistory;

import java.util.Optional;

public interface UserExamHistoryService {
    UserExamHistory save(UserExamHistory userExamHistory);

    Integer calculateScoreListening(Integer numberCorrectAnswer);

    Integer calculateScoreReading(Integer numberCorrectAnswer);

    Object findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);

    Object findUserExamHistoryByUserId(UserAccount profile);

    Object findAllUserExamHistoryByUser(UserAccount profile);

    Object findUserExamHistoryByUserIdAndExamId(UserAccount profile, Integer userExamHistoryId);
}
