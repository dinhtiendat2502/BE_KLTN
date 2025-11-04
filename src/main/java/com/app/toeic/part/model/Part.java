package com.app.toeic.part.model;


import com.app.toeic.exam.model.Exam;
import com.app.toeic.question.model.Question;
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
@Table(name = "part", indexes = {
        @Index(name = "part_exam_id_index", columnList = "exam_id"),
        @Index(name = "part_real_exam_id_index", columnList = "real_exam_id"),
        @Index(name = "part_code_index", columnList = "partCode")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer partId;
    String partName;
    String partCode;
    @Column(columnDefinition = "TEXT")
    String partImage;
    @Column(columnDefinition = "TEXT")
    String partAudio;
    @Column(columnDefinition = "TEXT")
    String partContent;
    int numberOfQuestion;

    @Builder.Default
    String status = Constant.STATUS_ACTIVE;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    Exam exam;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @OrderBy("questionNumber ASC")
    @Builder.Default
    Set<Question> questions = new HashSet<>();
}
