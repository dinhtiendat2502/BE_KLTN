package com.app.toeic.exam.payload;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FinishExamDTO {
    Integer examId;
    List<Answer> answers;
    Integer totalTime;
    Integer timeRemaining;
    Integer totalQuestion;
    Boolean isFullTest;
    Boolean isDone;
    String listPart;
    int totalLeave;
    int totalOpenNewTab;
    LocalDateTime startTime;
    LocalDateTime endTime;

    @Getter
    @Setter
    public static class Answer {
        Integer questionId;
        String answer;
        String partCode;
    }
}


