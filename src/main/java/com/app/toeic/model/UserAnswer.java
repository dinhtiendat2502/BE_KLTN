package com.app.toeic.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_answer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userAnswerId;

    private String selectedAnswer;
    private Boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_exam_history_id")
    private UserExamHistory userExamHistory;
}
