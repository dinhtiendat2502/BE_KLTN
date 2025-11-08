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
    @Builder.Default
    Integer numberOfCorrectAnswer = 0;
    @Builder.Default
    Integer numberOfWrongAnswer = 0;
    @Builder.Default
    Integer numberOfNotAnswer = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart1 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart2 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart3 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart4 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart5 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart6 = 0;
    @Builder.Default
    Integer numberOfCorrectAnswerPart7 = 0;
    @Builder.Default
    Integer numberOfCorrectListeningAnswer = 0;
    @Builder.Default
    Integer numberOfWrongListeningAnswer = 0;
    @Builder.Default
    Integer numberOfCorrectReadingAnswer = 0;
    @Builder.Default
    Integer numberOfWrongReadingAnswer = 0;
    @Builder.Default
    Integer totalScore = 0;
    @Builder.Default
    Integer totalScoreReading = 0;
    @Builder.Default
    Integer totalScoreListening = 0;
    Integer timeToDoExam;       // calculate by second
    Integer timeRemaining;      // calculate by second
    @Builder.Default
    Boolean isDone = false;
    @Builder.Default
    Boolean isFullTest = false;
    String listPart;

    @Builder.Default
    Integer totalLeave = 0;

    @Builder.Default
    Integer totalOpenNewTab = 0;

    LocalDateTime examDate;
    LocalDateTime endTime;

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
