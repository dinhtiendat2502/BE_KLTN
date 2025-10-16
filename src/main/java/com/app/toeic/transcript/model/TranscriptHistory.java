package com.app.toeic.transcript.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcript_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranscriptHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String jobRevAIId;
    String transcriptName;

    @Column(columnDefinition = "TEXT")
    String transcriptContent;

    @Column(columnDefinition = "TEXT")
    String transcriptContentTranslate;

    @Column(columnDefinition = "TEXT")
    String transcriptAudio;

    @CreationTimestamp
    LocalDateTime createdAt;

    @Builder.Default
    String status = "IN_PROGRESS";
}
