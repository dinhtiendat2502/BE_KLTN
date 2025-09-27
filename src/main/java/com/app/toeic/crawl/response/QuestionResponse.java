package com.app.toeic.crawl.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
    private Boolean questionHaveTranscript;
}
