package com.app.toeic.exam.model;


import com.app.toeic.part.model.Part;
import com.app.toeic.topic.model.Topic;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer examId;
    private String examName;
    private String examImage;
    
    @Column(columnDefinition = "TEXT")
    private String examAudio;
    @Column(columnDefinition = "TEXT")
    private String audioPart1;
    @Column(columnDefinition = "TEXT")
    private String audioPart2;
    @Column(columnDefinition = "TEXT")
    private String audioPart3;
    @Column(columnDefinition = "TEXT")
    private String audioPart4;

    @Builder.Default
    private String status = "ACTIVE";

    @Builder.Default
    private Integer numberOfUserDoExam = 0;

    @Builder.Default
    private Double price = 0.0;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @OrderBy("partId ASC")
    @Builder.Default
    private Set<Part> parts = new HashSet<>();
}
