package com.app.toeic.score.repo;

import com.app.toeic.score.model.CalculateScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICalculateScoreRepository extends JpaRepository<CalculateScore, Integer> {
}
