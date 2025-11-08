package com.app.toeic.score.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "calculate_score", indexes = {
        @Index(name = "total_question_index", columnList = "totalQuestion"),
})
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

    public static CalculateScore from(Integer i){
        return CalculateScore.builder()
                .totalQuestion(i)
                .scoreListening(0)
                .scoreReading(0)
                .build();
    }
}
