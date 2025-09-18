package com.app.toeic.dto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuestionDto implements Serializable {
    private Integer questionId;
    private String questionContent;
    private String paragraph1;
    private String paragraph2;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;
    private String questionImage;
    private String questionAudio;
}
