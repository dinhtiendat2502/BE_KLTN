package com.app.toeic.userexam.response;

import java.time.LocalDateTime;

/**
 * Projection for {@link com.app.toeic.userexam.model.UserExamHistory}
 */
public interface UserExamHistoryInfo {
    Integer getUserExamHistoryId();

    Integer getTotalQuestion();

    Integer getNumberOfCorrectAnswer();

    Integer getNumberOfWrongAnswer();

    Integer getNumberOfNotAnswer();

    Integer getNumberOfCorrectAnswerPart1();

    Integer getNumberOfCorrectAnswerPart2();

    Integer getNumberOfCorrectAnswerPart3();

    Integer getNumberOfCorrectAnswerPart4();

    Integer getNumberOfCorrectAnswerPart5();

    Integer getNumberOfCorrectAnswerPart6();

    Integer getNumberOfCorrectAnswerPart7();

    Integer getNumberOfCorrectListeningAnswer();

    Integer getNumberOfWrongListeningAnswer();

    Integer getNumberOfCorrectReadingAnswer();

    Integer getNumberOfWrongReadingAnswer();

    Integer getTotalScore();

    Integer getTotalScoreReading();

    Integer getTotalScoreListening();

    Integer getTimeToDoExam();

    Integer getTimeRemaining();

    String getListPart();

    Integer getTotalLeave();

    Integer getTotalOpenNewTab();

    LocalDateTime getExamDate();

    LocalDateTime getEndTime();

    UserExam getUser();

    Exam getExam();

    interface UserExam {
        String getEmail();
    }

    interface Exam {
        String getExamName();
    }
}
