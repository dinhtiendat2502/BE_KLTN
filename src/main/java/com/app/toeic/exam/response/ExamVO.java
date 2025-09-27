package com.app.toeic.exam.response;


import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
public class ExamVO implements Serializable {

    public interface ExamListAll {
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

    public interface ExamList {
        Integer getExamId();

        String getExamName();

        String getExamImage();

        String getAudioPart1();

        String getAudioPart2();

        String getAudioPart3();

        String getAudioPart4();

        String getStatus();
    }

    public interface ExamFullQuestion {
        Integer getExamId();

        String getExamName();

        String getExamImage();

        String getAudioPart1();

        String getAudioPart2();

        String getAudioPart3();

        String getAudioPart4();

        String getStatus();

        Set<Part> getParts();


        interface Part {
            Integer getPartId();

            String getPartName();

            String getPartCode();

            String getPartImage();

            String getPartAudio();

            String getPartContent();

            int getNumberOfQuestion();

            String getStatus();

            Set<Question> getQuestions();

            interface Question {
                Integer getQuestionId();

                String getQuestionNumber();

                String getQuestionContent();

                String getParagraph1();

                String getParagraph2();

                String getQuestionImage();

                String getQuestionAudio();

                String getAnswerA();

                String getAnswerB();

                String getAnswerC();

                String getAnswerD();
            }
        }
    }

    public interface ExamFullQuestionWithAnswer {
        Integer getExamId();

        String getExamName();


        String getStatus();

        Set<ExamFullQuestionWithAnswer.Part> getParts();

        interface Part {
            Integer getPartId();

            String getPartName();

            String getPartCode();

            int getNumberOfQuestion();

            String getStatus();

            Set<ExamFullQuestionWithAnswer.Part.Question> getQuestions();

            interface Question {
                Integer getQuestionId();

                String getQuestionNumber();

                String getCorrectAnswer();
            }
        }
    }
}
