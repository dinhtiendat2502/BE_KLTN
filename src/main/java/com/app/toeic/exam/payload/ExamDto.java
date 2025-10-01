package com.app.toeic.exam.payload;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ExamDTO implements Serializable {
    Integer examId;
    @NotEmpty(message = "Tên đề thi không được để trống")
    String examName;
    String examImage;
    String audioPart1;
    String audioPart2;
    String audioPart3;
    String audioPart4;
    Integer topicId;
}
