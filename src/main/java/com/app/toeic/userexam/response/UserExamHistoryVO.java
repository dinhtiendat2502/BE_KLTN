package com.app.toeic.userexam.response;


import java.time.LocalDateTime;
import java.util.Set;

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


    public interface UserExamHistoryDetail {
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
        Integer getTotalLeave();

        Integer getTotalOpenNewTab();

        ExamVo1 getExam();

        Set<UserAnswer> getUserAnswers();


        interface UserAnswer {
            Integer getUserAnswerId();

            String getSelectedAnswer();

            Boolean getIsCorrect();

            Question getQuestion();


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

                String getCorrectAnswer();

                String getTranscript();

                String getTranslateTranscript();
                Boolean getHaveMultiImage();

                Set<QuestionImageRS> getQuestionImages();

                interface QuestionImageRS {
                    Integer getQuestionImageId();

                    String getQuestionImage();
                }
            }
        }


        interface ExamVo1 {
            Integer getExamId();

            String getExamName();

            String getStatus();
        }
    }
}
