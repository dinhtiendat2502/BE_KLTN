package com.app.toeic.score.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "calculate_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer calculateScoreId;

    Integer totalQuestion;
    Integer scoreListening;
    Integer scoreReading;
}
