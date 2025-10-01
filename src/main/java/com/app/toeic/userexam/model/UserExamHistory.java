package com.app.toeic.userexam.model;


import com.app.toeic.exam.model.Exam;
import com.app.toeic.user.model.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
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
public class UserExamHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userExamHistoryId;
    private Integer totalQuestion;
    private Integer numberOfCorrectAnswer;
    private Integer numberOfWrongAnswer;
    private Integer numberOfNotAnswer;
    private Integer numberOfCorrectAnswerPart1;
    private Integer numberOfCorrectAnswerPart2;
    private Integer numberOfCorrectAnswerPart3;
    private Integer numberOfCorrectAnswerPart4;
    private Integer numberOfCorrectAnswerPart5;
    private Integer numberOfCorrectAnswerPart6;
    private Integer numberOfCorrectAnswerPart7;
    private Integer numberOfCorrectListeningAnswer;
    private Integer numberOfWrongListeningAnswer;
    private Integer numberOfCorrectReadingAnswer;
    private Integer numberOfWrongReadingAnswer;
    private Integer totalScore;
    private Integer totalScoreReading;
    private Integer totalScoreListening;
    private Integer timeToDoExam;       // calculate by second
    private Integer timeRemaining;      // calculate by second
    @Builder.Default
    private Boolean isDone = false;
    @Builder.Default
    private Boolean isFullTest = false;
    private String listPart;

    @CreationTimestamp
    private LocalDateTime examDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @OrderBy("examId ASC")
    private Exam exam;

    @OneToMany(mappedBy = "userExamHistory", cascade = CascadeType.ALL)
    @JsonBackReference
    @OrderBy("userAnswerId ASC")
    private Set<UserAnswer> userAnswers = new HashSet<>();
}
