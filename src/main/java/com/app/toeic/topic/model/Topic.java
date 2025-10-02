package com.app.toeic.topic.model;

import com.app.toeic.exam.model.Exam;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    private String topicName;
    private String topicImage;
    @Builder.Default
    private String status = "ACTIVE";
    @Builder.Default
    private Boolean isFree = true;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "topic")
    @JsonBackReference
    @Builder.Default
    private Set<Exam> exams = new HashSet<>();
}
