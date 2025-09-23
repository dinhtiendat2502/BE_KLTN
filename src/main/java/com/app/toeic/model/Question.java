package com.app.toeic.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.io.Serializable;
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
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;
    private String questionNumber;
    @Column(columnDefinition = "TEXT")
    private String questionContent;
    @Column(columnDefinition = "TEXT")
    private String paragraph1;
    @Column(columnDefinition = "TEXT")
    private String paragraph2;
    @Column(columnDefinition = "TEXT")
    private String questionImage;
    @Column(columnDefinition = "TEXT")
    private String questionAudio;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;

    private String transcript;
    private String transcriptAudio;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "part_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @org.hibernate.annotations.Index(name = "part_id_index")
    @JsonBackReference
    private Part part;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @org.hibernate.annotations.IndexColumn(name = "question_image_index")
    @JsonManagedReference
    private Set<QuestionImage> questionImages = new HashSet<>();
}
