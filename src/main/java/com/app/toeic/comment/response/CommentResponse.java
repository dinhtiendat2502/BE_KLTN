package com.app.toeic.comment.response;

import java.time.LocalDateTime;

public interface CommentResponse {

    interface CommentVO {
        Long getCommentId();

        String getContent();

        LocalDateTime getCreatedAt();

        UserVO getUser();

        ExamVO getExam();

        CommentVO getParent();

        String getStatus();
    }
    interface ReplyVO {
        Long getCommentId();
        String getContent();
        UserVO getUser();
        LocalDateTime getCreatedAt();
        String getStatus();
    }
    interface UserVO {
        Long getUserId();

        String getFullName();

        String getAvatar();
    }
    interface ExamVO {
        Integer getExamId();

        String getExamName();
    }
}
