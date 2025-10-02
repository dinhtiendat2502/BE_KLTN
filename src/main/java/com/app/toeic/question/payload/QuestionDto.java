package com.app.toeic.question.payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
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
