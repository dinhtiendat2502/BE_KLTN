package com.app.toeic.score.repo;

import com.app.toeic.score.model.CalculateScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICalculateScoreRepository extends JpaRepository<CalculateScore, Integer> {
    @Query("""
            SELECT a
            FROM CalculateScore a
        """)
    List<CalculateScore> findAllByTotalQuestion(Integer totalQuestionReading, Integer totalQuestionListening);
}
