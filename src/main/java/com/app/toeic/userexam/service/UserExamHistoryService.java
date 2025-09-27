package com.app.toeic.userexam.service;

import com.app.toeic.user.model.UserAccount;
import com.app.toeic.userexam.model.UserExamHistory;

public interface UserExamHistoryService {
    UserExamHistory save(UserExamHistory userExamHistory);

    Integer calculateScoreListening(Integer numberCorrectAnswer);

    Integer calculateScoreReading(Integer numberCorrectAnswer);

    Object findUserExamHistoryByExamHistoryId(Integer userExamHistoryId);

    Object findUserExamHistoryByUserId(UserAccount profile);

    Object findAllUserExamHistoryByUser(UserAccount profile);

    Object findUserExamHistoryByUserIdAndExamId(UserAccount profile, Integer userExamHistoryId);
}
