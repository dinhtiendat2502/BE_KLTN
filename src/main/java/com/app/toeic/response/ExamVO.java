package com.app.toeic.response;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ExamVO implements Serializable {

    public interface ExamListAll{
        Integer getExamId();
        String getExamName();
        String getExamImage();
        String getAudioPart1();
        String getAudioPart2();
        String getAudioPart3();
        String getAudioPart4();
        Topic getTopic();
        String getStatus();
        interface Topic {
            Integer getTopicId();
            String getTopicName();
            String getTopicImage();
            String getStatus();
        }
    }
}
