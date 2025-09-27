package com.app.toeic.score.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "calculate_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculateScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calculateScoreId;

    private Integer totalQuestion;
    private Integer scoreListening;
    private Integer scoreReading;
}
