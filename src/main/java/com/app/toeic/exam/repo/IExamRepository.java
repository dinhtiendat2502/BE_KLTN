package com.app.toeic.exam.repo;

import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.response.ExamVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Integer> {
    @Query("SELECT COUNT(e) > 0 FROM Exam e WHERE e.examName = ?1 AND e.examId <> ?2")
    Boolean existsExamByExamName(String examName, Integer examId);

    @Query("SELECT e FROM Exam e LEFT JOIN FETCH e.topic t WHERE e.status = 'ACTIVE' ORDER BY t.topicId DESC, e.examName ASC")
    List<ExamVO.ExamListAll> findAllByStatus(String status);

    @Query("SELECT e FROM Exam e LEFT JOIN FETCH e.topic t WHERE e.status = 'ACTIVE' AND t.topicId = ?1 ORDER BY t.topicId DESC, e.examName ASC")
    List<ExamVO.ExamListAll> findAllByStatusAndTopicId(Integer topicId);

    @Query("""
            SELECT e
            FROM Exam e
            WHERE e.status = 'ACTIVE' AND e.topic is NULL
            ORDER BY e.examName ASC
            """)
    List<ExamVO.ExamListAll> findAllByTopicIsNull();

    @Query("SELECT e FROM Exam e JOIN FETCH e.parts p WHERE e.examId = ?1")
    Optional<Exam> findExamWithPart(Integer examId);

    @Query("""
                    SELECT e
                    FROM Exam e
                    JOIN fetch e.parts
                    WHERE e.examId = ?1
            """)
    Optional<ExamVO.ExamList> findExamByExamId(Integer examId);

    @Query("""
            SELECT e
            FROM Exam e
            JOIN FETCH e.parts p
            JOIN FETCH p.questions q
            LEFT JOIN FETCH q.questionImages
            WHERE e.examId = ?1 AND e.status = 'ACTIVE'
            ORDER BY p.partCode ASC, q.questionNumber ASC
            """)
    Optional<ExamVO.ExamFullQuestion> findExamWithFullQuestion(Integer examId);

    @Query("SELECT e FROM Exam e JOIN FETCH e.parts p JOIN FETCH p.questions q WHERE e.examId = ?1 ORDER BY p.partCode ASC, q.questionNumber ASC")
    Optional<ExamVO.ExamFullQuestionWithAnswer> findExamFullQuestionWithAnswer(Integer examId);


    @Query("""
            SELECT e 
            FROM Exam e 
            JOIN FETCH e.parts p 
            JOIN FETCH p.questions q 
            LEFT JOIN FETCH q.questionImages
            WHERE e.examId = ?1 AND e.status = 'ACTIVE' AND p.partCode IN (?2) 
            ORDER BY p.partCode ASC, q.questionNumber ASC
            """)
    Optional<ExamVO.ExamFullQuestion> findExamPractice(Integer examId, List<String> listPart);


    @Query("""
            SELECT e
            FROM Exam e
            WHERE e.status = 'ACTIVE' AND e.isFree = ?1
            ORDER BY e.examName ASC
            """)
    List<Exam> findAllByFree(boolean isFree);

}
