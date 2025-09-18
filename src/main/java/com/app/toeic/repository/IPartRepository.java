package com.app.toeic.repository;


import com.app.toeic.model.Exam;
import com.app.toeic.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPartRepository extends JpaRepository<com.app.toeic.model.Part, Integer> {
    Boolean existsByExamAndPartName(Exam exam, String partName);

    @Query("SELECT p FROM Part p JOIN FETCH p.questions WHERE p.exam.examId = ?1")
    List<Part> findAllByExamWithQuestions(Integer examId);

    @Query("SELECT p FROM Part p LEFT JOIN FETCH p.questions WHERE p.partId = ?1")
    Optional<Part> findPartWithQuestion(Integer partId);
}
