package com.app.toeic.topic.model;

import com.app.toeic.exam.model.Exam;
import com.app.toeic.util.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer topicId;

    String topicName;
    String topicImage;
    @Builder.Default
    String status = Constant.STATUS_ACTIVE;
    @Builder.Default
    Boolean isFree = true;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "topic")
    @JsonBackReference
    @Builder.Default
    Set<Exam> exams = new HashSet<>();
}
