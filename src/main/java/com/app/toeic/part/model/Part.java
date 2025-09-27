package com.app.toeic.part.model;


import com.app.toeic.exam.model.Exam;
import com.app.toeic.question.model.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "part")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partId;
    private String partName;
    private String partCode;
    @Column(columnDefinition = "TEXT")
    private String partImage;
    @Column(columnDefinition = "TEXT")
    private String partAudio;
    @Column(columnDefinition = "TEXT")
    private String partContent;
    private int numberOfQuestion;
    @Builder.Default
    private String status = "ACTIVE";
    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @org.hibernate.annotations.Index(name = "exam_id_index")
    private Exam exam;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    @JsonBackReference
    @OrderBy("questionNumber ASC")
    private Set<Question> questions = new HashSet<>();
}
