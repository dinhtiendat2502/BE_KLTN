package com.app.toeic.question.model;


import com.app.toeic.part.model.Part;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer questionId;
    Integer questionNumber;
    @Column(columnDefinition = "TEXT")
    String questionContent;
    @Column(columnDefinition = "TEXT")
    String paragraph1;
    @Column(columnDefinition = "TEXT")
    String paragraph2;
    @Column(columnDefinition = "TEXT")
    String questionImage;
    @Column(columnDefinition = "TEXT")
    String questionAudio;
    String answerA;
    String answerB;
    String answerC;
    String answerD;
    String correctAnswer;

    @Builder.Default
    Boolean questionHaveTranscript = false;

    @Builder.Default
    Boolean haveMultiImage = false;

    @Builder.Default
    Integer numberQuestionInGroup = 1;

    @Column(columnDefinition = "TEXT")
    String transcript;
    @Column(columnDefinition = "TEXT")
    String translateTranscript;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "part_id")
    Part part;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Builder.Default
    Set<QuestionImage> questionImages = new HashSet<>();
}
