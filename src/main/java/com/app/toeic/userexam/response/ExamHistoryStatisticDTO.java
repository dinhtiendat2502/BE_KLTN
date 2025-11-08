package com.app.toeic.userexam.response;

import java.time.LocalDateTime;

public interface ExamHistoryStatisticDTO {
    Integer getUserExamHistoryId();

    Integer getTotalScore();

    Integer getTotalScoreReading();

    Integer getTotalScoreListening();

    Boolean getIsFullTest();

    LocalDateTime getExamDate();

    String getListPart();

    UserExamDTO getUser();

    interface UserExamDTO {
        Integer getuserId();

        String getEmail();

        String getFullName();
    }
}
