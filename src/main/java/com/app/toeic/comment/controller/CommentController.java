package com.app.toeic.comment.controller;

import com.app.toeic.comment.model.Comment;
import com.app.toeic.comment.repo.CommentRepository;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentRepository commentRepository;
    UserService userService;

    @PostMapping("/create")
    public Object createComment(
            HttpServletRequest request,
            @RequestBody CommentPayload payload
    ) {
        var user = userService.getProfile(request);
        if (user.isEmpty()) {
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("USER_NOT_LOGIN")
                    .build();
        }

        var comment = Comment.builder()
                .content(payload.content)
                .exam(Exam.builder().examId(payload.examId).build())
                .parent(payload.parentId == null ? null : Comment.builder().commentId(payload.parentId).build())
                .user(user.get())
                .build();
        commentRepository.save(comment);
        return ResponseVO
                .builder()
                .success(true)
                .message("CREATE_COMMENT_SUCCESS")
                .build();
    }

    @GetMapping("get-by-exam")
    public Object getCommentByExam(
            @RequestParam("examId") Integer examId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return commentRepository.findAllByExamExamId(examId, PageRequest.of(page, size));
    }

    public record CommentPayload(String content, Integer examId, Long parentId) {
    }
}
