package com.app.toeic.exam.model;

import com.app.toeic.part.model.Part;
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
@Table(name = "real_exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RealExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long realExamId;

    String examName;
    String examImage;

    @Column(columnDefinition = "TEXT")
    String examAudio;

    @Builder.Default
    String status = Constant.STATUS_ACTIVE;

    @Builder.Default
    Integer numberOfUserDoExam = 0;

    LocalDateTime fromDate;
    LocalDateTime toDate;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "realExam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @OrderBy("partCode ASC")
    @Builder.Default
    Set<Part> parts = new HashSet<>();
}
