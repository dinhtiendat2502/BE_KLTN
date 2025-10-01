package com.app.toeic.crawl.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private Integer questionId;
    private String questionNumber;
    private String questionContent;
    private String paragraph1;
    private String paragraph2;
    private String questionImage;
    private String questionAudio;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;

    private String transcript;
    private String translateTranscript;
    @Builder.Default
    private Boolean questionHaveTranscript = false;
    @Builder.Default
    private Boolean haveMultiImage = false;
    @Builder.Default
    private List<String> questionImages = new ArrayList<>();
}
