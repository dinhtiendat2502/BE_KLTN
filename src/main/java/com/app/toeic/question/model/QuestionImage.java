package com.app.toeic.question.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionImageId;
    @Column(columnDefinition = "TEXT")
    private String questionImage;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
