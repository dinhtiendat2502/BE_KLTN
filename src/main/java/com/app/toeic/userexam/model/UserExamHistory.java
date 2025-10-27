package com.app.toeic.userexam.model;


import com.app.toeic.exam.model.Exam;
import com.app.toeic.user.model.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_exam_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserExamHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userExamHistoryId;
    Integer totalQuestion;
    Integer numberOfCorrectAnswer;
    Integer numberOfWrongAnswer;
    Integer numberOfNotAnswer;
    Integer numberOfCorrectAnswerPart1;
    Integer numberOfCorrectAnswerPart2;
    Integer numberOfCorrectAnswerPart3;
    Integer numberOfCorrectAnswerPart4;
    Integer numberOfCorrectAnswerPart5;
    Integer numberOfCorrectAnswerPart6;
    Integer numberOfCorrectAnswerPart7;
    Integer numberOfCorrectListeningAnswer;
    Integer numberOfWrongListeningAnswer;
    Integer numberOfCorrectReadingAnswer;
    Integer numberOfWrongReadingAnswer;
    Integer totalScore;
    Integer totalScoreReading;
    Integer totalScoreListening;
    Integer timeToDoExam;       // calculate by second
    Integer timeRemaining;      // calculate by second
    @Builder.Default
    Boolean isDone = false;
    @Builder.Default
    Boolean isFullTest = false;
    String listPart;

    @CreationTimestamp
    LocalDateTime examDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    UserAccount user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @OrderBy("examId ASC")
    Exam exam;

    @OneToMany(mappedBy = "userExamHistory", cascade = CascadeType.ALL)
    @JsonBackReference
    @OrderBy("userAnswerId ASC")
    @Builder.Default
    Set<UserAnswer> userAnswers = new HashSet<>();
}
