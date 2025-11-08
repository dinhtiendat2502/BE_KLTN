package com.app.toeic.part.repo;


import com.app.toeic.exam.response.PartResponse;
import com.app.toeic.part.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPartRepository extends JpaRepository<Part, Integer> {
    @Query("SELECT p FROM Part p JOIN FETCH p.questions WHERE p.exam.examId = ?1")
    List<Part> findAllByExamWithQuestions(Integer examId);

    @Query("SELECT p FROM Part p LEFT JOIN FETCH p.questions WHERE p.partId = ?1")
    Optional<Part> findPartWithQuestion(Integer partId);

    @Query("""
                SELECT p
                FROM Part p
                JOIN FETCH p.questions q
                LEFT JOIN FETCH q.questionImages
                WHERE p.partId = ?1
            """)
    Optional<PartResponse> findPartById(Integer partId);

    @Query("""
                SELECT p
                FROM Part p
                JOIN FETCH p.exam
                JOIN FETCH p.questions q
                LEFT JOIN FETCH q.questionImages
                WHERE p.exam.examId = ?1
            """)
    List<PartResponse> findAllByExamExamId(Integer examId);


    @Query("""
                SELECT p
                FROM Part p
                JOIN FETCH p.exam
                JOIN FETCH p.questions q
                LEFT JOIN FETCH q.questionImages
                WHERE p.exam.status = 'ACTIVE' AND p.exam.isFree = ?1
            """)
    List<PartResponse> findAllPartWithExamNotFree(boolean isFree);

}
