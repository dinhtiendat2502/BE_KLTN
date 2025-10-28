package com.app.toeic.comment.response;

import java.time.LocalDateTime;

public interface CommentResponse {

    interface CommentVO {
        Long getCommentId();

        String getContent();

        LocalDateTime getCreatedAt();

        UserVO getUser();

        Integer getNumberOfReplies();

        String getStatus();
    }

    interface Comment2VO {
        Long getCommentId();
        Integer getNumberOfReplies();
    }

    interface UserVO {
        Long getUserId();

        String getFullName();

        String getAvatar();
    }
}
