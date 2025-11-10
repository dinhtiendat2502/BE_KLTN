package com.app.toeic.userexam.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.userexam.model.UserExamHistory;
import com.app.toeic.userexam.repo.IUserExamHistoryRepository;
import com.app.toeic.userexam.service.UserExamHistoryService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExamHistoryServiceImpl implements UserExamHistoryService {
    private final IUserExamHistoryRepository userExamHistoryRepository;

    @Override
    public UserExamHistory save(UserExamHistory userExamHistory) {
        return userExamHistoryRepository.save(userExamHistory);
    }

    @Override
    public Integer calculateScoreListening(Integer numberCorrectAnswer) {
        return 0;
    }

    @Override
    public Integer calculateScoreReading(Integer numberCorrectAnswer) {
        return 0;
    }

    @Override
    public Object findUserExamHistoryByExamHistoryId(Integer userExamHistoryId) {
        return userExamHistoryRepository
                .findUserExamHistoryByExamHistoryId(userExamHistoryId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "EXAM_HISTORY_NOT_FOUND"));
    }

    @Override
    public Object findUserExamHistoryByUserId(UserAccount profile) {
        return userExamHistoryRepository.findAllByUser(profile);
    }

    @Override
    public Object findAllUserExamHistoryByUser(UserAccount profile) {
        return userExamHistoryRepository.findAllByUser(profile);
    }

    @Override
    public Object findUserExamHistoryByUserIdAndExamId(UserAccount profile, Integer userExamHistoryId) {
        return userExamHistoryRepository
                .findByUserExamHistoryId(profile, userExamHistoryId)
                .orElse(null);
    }

    @Override
    public Object findUserExamHistoryByUserIdAndExamId(Integer userExamHistoryId) {
        return userExamHistoryRepository.findByUserExamHistoryId(userExamHistoryId).orElse(null);
    }

    @Override
    public Object findAllUserExamHistory() {
        return userExamHistoryRepository
                .findAllExamHistory();
    }
}

