package com.app.toeic.exam.model;


import com.app.toeic.comment.model.Comment;
import com.app.toeic.part.model.Part;
import com.app.toeic.topic.model.Topic;
import com.app.toeic.userexam.model.UserExamHistory;
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
@Table(name = "exam", indexes = {
        @Index(name = "status_index", columnList = "status"),
        @Index(name = "is_free_index", columnList = "is_free"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer examId;
    String examName;
    String examImage;

    @Column(columnDefinition = "TEXT")
    String examAudio;
    @Column(columnDefinition = "TEXT")
    String audioPart1;
    @Column(columnDefinition = "TEXT")
    String audioPart2;
    @Column(columnDefinition = "TEXT")
    String audioPart3;
    @Column(columnDefinition = "TEXT")
    String audioPart4;

    @Builder.Default
    String status = Constant.STATUS_PENDING;

    @Builder.Default
    Integer numberOfUserDoExam = 0;

    @Builder.Default
    Double price = 0.0;

    @Builder.Default
    boolean isFree = false;

    LocalDateTime fromDate;
    LocalDateTime toDate;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    Topic topic;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @OrderBy("partCode ASC")
    @Builder.Default
    Set<Part> parts = new HashSet<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Builder.Default
    Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Builder.Default
    Set<UserExamHistory> userExamHistories = new HashSet<>();
}
