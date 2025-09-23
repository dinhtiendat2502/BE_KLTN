package com.app.toeic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICalculateScoreRepository extends JpaRepository<com.app.toeic.model.CalculateScore, Integer> {
}
