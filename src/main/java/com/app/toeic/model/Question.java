package com.app.toeic.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;
    private String questionName;
    @Column(columnDefinition = "TEXT")
    private String questionContent;
    private String questionImage;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    @Column(columnDefinition = "TEXT")
    private String paragraph;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    @JsonIgnore
    private Part part;
}
