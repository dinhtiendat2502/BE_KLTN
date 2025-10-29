package com.app.toeic.score.repo;

import com.app.toeic.score.model.CalculateScore;
import com.app.toeic.score.response.ScoreRepsonse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICalculateScoreRepository extends JpaRepository<CalculateScore, Integer> {
    @Query(value = """
            select score_reading as reading,
                    0             as listening
             from calculate_score
             where total_question = :totalQuestionReading
             union all
             select 0               as reading,
                    score_listening as listening
             from calculate_score
             where total_question = :totalQuestionListening
            """, nativeQuery = true)
    List<ScoreRepsonse> findAllByTotalQuestion(@Param("totalQuestionReading") Integer totalQuestionReading, @Param("totalQuestionListening") Integer totalQuestionListening);
}
