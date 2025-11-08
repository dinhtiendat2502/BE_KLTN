package com.app.toeic.userexam.model;

import com.app.toeic.question.model.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "user_answer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userAnswerId;

    String selectedAnswer;
    Boolean isCorrect;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "question_id")
    Question question;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_exam_history_id")
    UserExamHistory userExamHistory;
}
