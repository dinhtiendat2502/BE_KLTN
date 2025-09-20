package com.app.toeic.response;


import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class UserExamHistoryVO {
    public interface UserExamHistoryGeneral {
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

        LocalDateTime getExamDate();

        ExamVo1 getExam();

        interface ExamVo1 {
            Integer getExamId();

            String getExamName();

            String getStatus();
        }
    }
}
