package com.app.toeic.controller.admin;


import com.app.toeic.model.CalculateScore;
import com.app.toeic.repository.ICalculateScoreRepository;
import com.app.toeic.response.ResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/score")
public class ScoreController {
    private final ICalculateScoreRepository calculateScoreRepository;

    @GetMapping("/init")
    public ResponseVO initScore() {
        calculateScoreRepository.deleteAll();
        var initList = IntStream.range(0, 101)
                .mapToObj(i -> CalculateScore
                        .builder()
                        .totalQuestion(i)
                        .scoreListening(0)
                        .scoreReading(0)
                        .build())
                .toList();
        calculateScoreRepository.saveAll(initList);
        return ResponseVO.builder().success(Boolean.TRUE).message("Init score success").build();
    }

    @GetMapping("/list")
    public ResponseVO findAll() {
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .data(calculateScoreRepository.findAll())
                .message("Init score success")
                .build();
    }
}
