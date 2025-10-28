package com.app.toeic.comment.repo;

import com.app.toeic.comment.model.Comment;
import com.app.toeic.comment.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
                SELECT c
                FROM Comment c
                JOIN FETCH c.user
                WHERE c.exam.examId = :examId AND c.parent IS NULL
                ORDER BY c.createdAt DESC
            """)
    Page<CommentResponse.CommentVO> findAllByExamExamId(Integer examId, Pageable pageable);

    @Query("""
                SELECT c
                FROM Comment c
                LEFT JOIN FETCH c.parent
                WHERE c.commentId = :commentId
            """)
    Optional<Comment> findByCommentId(Long commentId);


    @Query("""
                SELECT c
                FROM Comment c
                JOIN FETCH c.user
                WHERE c.parent.commentId = :parentCommentId
                ORDER BY c.createdAt ASC
            """)
    Page<CommentResponse.CommentVO> findAllByParentCommentId(Long parentCommentId, Pageable pageable);
}
