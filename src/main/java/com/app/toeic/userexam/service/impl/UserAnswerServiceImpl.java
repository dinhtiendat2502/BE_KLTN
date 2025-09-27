package com.app.toeic.userexam.service.impl;

import com.app.toeic.userexam.model.UserAnswer;
import com.app.toeic.userexam.repo.IUserAnswerRepository;
import com.app.toeic.userexam.service.UserAnswerService;
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
