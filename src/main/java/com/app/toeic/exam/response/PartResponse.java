package com.app.toeic.exam.response;

import java.util.Set;

public interface PartResponse {
    Integer getPartId();

    String getPartName();
    String getPartCode();
    String getPartContent();
    int getNumberOfQuestion();
    ExamResponse getExam();
    Set<ExamVO.QuestionFull> getQuestions();

    interface ExamResponse {
        String getExamAudio();
    }
}
