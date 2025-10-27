package com.app.toeic.comment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CommentVO {
    Long commentId;
    String content;
    String createdAt;
    UserVO user;
    ExamVO exam;
    CommentVO parent;
    String status;
    Integer numberOfReplies;

    Set<ReplyVO> replies;

    @Getter
    @Setter
    public static class UserVO {
        Integer userId;
        String fullName;
        String avatar;
    }

    @Getter
    @Setter
    public static class ExamVO {
        Integer examId;
        String examName;
        String examImage;
    }

    @Getter
    @Setter
    public static class ReplyVO {
        Long commentId;
        String content;
        String createdAt;
        UserVO user;
        String status;
        Integer numberOfReplies;
    }
}
