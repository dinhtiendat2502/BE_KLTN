package com.app.toeic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.model.UserAccount;
import com.app.toeic.model.UserExamHistory;
import com.app.toeic.repository.ITopicRepository;
import com.app.toeic.repository.IUserExamHistoryRepository;
import com.app.toeic.service.UserExamHistoryService;
import com.app.toeic.util.HttpStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy lịch sử thi"));
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
        return userExamHistoryRepository.findByUserExamHistoryId(profile, userExamHistoryId).orElse(null);
    }


    @Getter
    @Setter
    @Builder
    static class Score {
        Integer numberCorrectAnswer;
        Integer score;
    }
}

