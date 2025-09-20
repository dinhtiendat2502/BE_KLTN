package com.app.toeic.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
    private Integer timeToDoExam;       // calculate by second
    private Integer timeRemaining;      // calculate by second

    @CreationTimestamp
    private LocalDateTime examDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exam exam;

    @OneToMany(mappedBy = "userExamHistory", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserAnswer> userAnswers = new HashSet<>();
}
