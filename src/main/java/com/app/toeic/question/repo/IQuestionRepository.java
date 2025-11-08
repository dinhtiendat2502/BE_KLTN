package com.app.toeic.question.repo;

import com.app.toeic.part.model.Part;
import com.app.toeic.question.mapping.QuestionInfo;
import com.app.toeic.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IQuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByPart(Part part);

    @Query("""
                    SELECT q
                    FROM Question q
                    JOIN FETCH q.part p
                    LEFT JOIN FETCH q.questionImages
                    WHERE p.partId = :part
                    ORDER BY q.questionNumber
            """)
    List<QuestionInfo> findAllByPartV2(Integer part);

    void deleteAllByPart(Part part);
}
