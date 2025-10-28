package com.app.toeic.comment.repo;

import com.app.toeic.comment.model.Comment;
import com.app.toeic.comment.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
                SELECT c
                FROM Comment c
                JOIN FETCH c.exam
                JOIN FETCH c.user
                WHERE c.exam.examId = :examId AND c.parent IS NULL
                ORDER BY c.createdAt DESC
            """)
    Page<CommentResponse.CommentVO> findAllByExamExamId(Integer examId, Pageable pageable);

    List<Comment> findAllByParent(Comment parent);
}
