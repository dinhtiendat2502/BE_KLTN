package com.app.toeic.repository;

import com.app.toeic.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Integer> {
    Boolean existsExamByExamName(String examName);
}
