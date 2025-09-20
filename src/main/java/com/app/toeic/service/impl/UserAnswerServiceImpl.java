package com.app.toeic.service.impl;

import com.app.toeic.model.UserAnswer;
import com.app.toeic.repository.IUserAnswerRepository;
import com.app.toeic.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerService {
    private final IUserAnswerRepository userAnswerRepository;

    @Override
    public void saveAll(List<UserAnswer> list) {
        userAnswerRepository.saveAll(list);
    }
}
