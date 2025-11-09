package com.app.toeic.question.mapping;

import java.util.Set;

/**
 * Projection for {@link com.app.toeic.question.model.Question}
 */
public interface QuestionInfo {
    Integer getQuestionId();

    Integer getQuestionNumber();

    String getQuestionContent();

    String getParagraph1();

    String getParagraph2();

    String getQuestionImage();

    String getQuestionAudio();

    String getAnswerA();

    String getAnswerB();

    String getAnswerC();

    String getAnswerD();

    String getCorrectAnswer();

    Boolean getQuestionHaveTranscript();

    Boolean getHaveMultiImage();

    Integer getNumberQuestionInGroup();

    String getTranscript();

    String getTranslateTranscript();

    Set<QuestionImageInfo> getQuestionImages();
}
