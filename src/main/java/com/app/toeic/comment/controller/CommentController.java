package com.app.toeic.comment.controller;

import com.app.toeic.comment.model.Comment;
import com.app.toeic.comment.repo.CommentRepository;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

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
        var parentOptional = commentRepository.findByCommentId(payload.parentId);
        var comment = Comment.builder()
                             .content(payload.content)
                             .exam(Exam.builder().examId(payload.examId).build())
                             .user(user.get())
                             .build();
        AtomicReference<Comment> log = new AtomicReference<>();
        parentOptional
                .ifPresentOrElse(c -> {
                    if (c.getParent() != null) {
                        var parent = c.getParent();
                        comment.setParent(parent);
                        var returnComment = commentRepository.save(comment);
                        parent.setNumberOfReplies(parent.getNumberOfReplies() + 1);
                        commentRepository.save(parent);
                        log.set(returnComment);
                    } else {
                        comment.setParent(c);
                        var returnComment = commentRepository.save(comment);
                        c.setNumberOfReplies(c.getNumberOfReplies() + 1);
                        commentRepository.save(c);
                        log.set(returnComment);
                    }
                }, () -> {
                    comment.setParent(null);
                    var returnComment = commentRepository.save(comment);
                    log.set(returnComment);
                });
        return ResponseVO
                .builder()
                .success(true)
                .data(log.get().getParent() != null ? log.get().getParent().getCommentId() : log.get().getCommentId())
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

    @GetMapping("get-by-parent")
    public Object getCommentByParent(
            @RequestParam("parentId") Long parentId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return commentRepository.findAllByParentCommentId(
                parentId,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }

    public record CommentPayload(String content, Integer examId, Long parentId) {
    }
}
