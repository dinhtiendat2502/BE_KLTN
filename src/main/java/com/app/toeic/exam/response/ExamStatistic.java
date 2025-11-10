package com.app.toeic.exam.response;
public record ExamStatistic(Integer examId, String examName, Integer numberOfUserDoExam, Integer maxScore, Integer minScore, Long percent) {
}
