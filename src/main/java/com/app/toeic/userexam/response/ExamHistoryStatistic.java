package com.app.toeic.userexam.response;

public record ExamHistoryStatistic(Integer userExamHistoryId,
                                   Integer totalScore,
                                   Integer totalScoreReading,
                                   Integer totalScoreListening,
                                   Boolean isFullTest,
                                   String listPart,
                                   UserExam user) {
    public record UserExam(Integer userId, String email, String fullName) {
    }
}
