package com.app.toeic.exam.repo;

import com.app.toeic.exam.model.RealExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealExamRepository extends JpaRepository<RealExam, Long> {
}
