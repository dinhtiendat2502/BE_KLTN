package com.app.toeic.repository;

import com.app.toeic.model.Exam;
import com.app.toeic.response.ExamVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Integer> {
    Boolean existsExamByExamName(String examName);

    @Query("SELECT e FROM Exam e JOIN FETCH e.topic t WHERE e.status = 'ACTIVE'")
    List<ExamVO.ExamListAll> findAllByStatus(String status);

    @Query("SELECT e FROM Exam e JOIN FETCH e.parts p WHERE e.examId = ?1")
    Optional<Exam> findExamWithPart(Integer examId);
}
