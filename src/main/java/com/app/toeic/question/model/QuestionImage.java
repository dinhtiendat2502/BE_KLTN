package com.app.toeic.question.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "question_image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer questionImageId;
    @Column(columnDefinition = "TEXT")
    String questionImage;

    @ManyToOne
    @JoinColumn(name = "question_id")
    Question question;
}
