package com.app.toeic.response;

import java.time.LocalDateTime;

public class MyExamVO {
    public interface MyExamList {
        Integer getUserExamHistoryId();

        LocalDateTime getExamDate();

        Integer getNumberOfCorrectAnswer();

        Integer getTotalQuestion();

        Integer getTimeToDoExam();

        Integer getTimeRemaining();

        Exam getExam();

        interface Exam {
            String getExamName();
        }
    }
}
