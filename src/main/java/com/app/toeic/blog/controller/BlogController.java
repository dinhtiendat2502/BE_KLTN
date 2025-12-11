package com.app.toeic.blog.controller;

import com.app.toeic.blog.model.Blog;
import com.app.toeic.blog.repo.BlogRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.service.FirebaseStorageService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Level;

@Log
@RestController
@RequestMapping("blog")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {
    BlogRepository blogRepository;
    FirebaseStorageService firebaseStorageService;

    @GetMapping("all")
    public Object getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort
    ) {
        var sortPage = "ASC".equalsIgnoreCase(sort) ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        var pageable = org.springframework.data.domain.PageRequest.of(page, size, sortPage);
        return blogRepository.findAll(pageable);
    }

    @GetMapping("detail/{blogId}")
    public Object getDetail(@PathVariable("blogId") Long blogId) {
        return ResponseVO
                .builder()
                .success(true)
                .data(blogRepository.findById(blogId).orElse(null))
                .build();
    }

    @PostMapping("create")
    public Object create(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("author") String author,
            @RequestParam("summary") String summary,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        var blog = Blog
                .builder()
                .title(title)
                .content(content)
                .author(author)
                .summary(summary)
                .build();
        try {
            if (file != null && !file.isEmpty()) {
                var imageUrl = firebaseStorageService.uploadFile(file);
                blog.setThumbnail(imageUrl);
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, "BlogController >> create >> IOException: ", ex);
        }
        blogRepository.save(blog);
        return ResponseVO
                .builder()
                .success(true)
                .message("CREATE_BLOG_SUCCESS")
                .build();
    }

    @PatchMapping("update")
    public Object update(
            @RequestParam("blogId") Long blogId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("author") String author,
            @RequestParam("summary") String summary,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        var blog = blogRepository.findById(blogId).orElse(null);
        if (blog == null) {
            return ResponseVO
                    .builder()
                    .success(false)
                    .message("BLOG_NOT_FOUND")
                    .build();
        }
        blog.setTitle(StringUtils.defaultIfBlank(title, blog.getTitle()));
        blog.setContent(StringUtils.defaultIfBlank(content, blog.getContent()));
        blog.setAuthor(StringUtils.defaultIfBlank(author, blog.getAuthor()));
        blog.setSummary(StringUtils.defaultIfBlank(summary, blog.getSummary()));
        try {
            if (file != null && !file.isEmpty()) {
                var imageUrl = firebaseStorageService.uploadFile(file);
                blog.setThumbnail(imageUrl);
            }
        } catch (IOException ex) {
            log.log(Level.WARNING, "BlogController >> update >> IOException: ", ex);
        }
        blogRepository.save(blog);
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_BLOG_SUCCESS")
                .build();
    }

    @DeleteMapping("delete/{blogId}")
    public Object delete(@PathVariable("blogId") Long blogId) {
        blogRepository.deleteById(blogId);
        return ResponseVO
                .builder()
                .success(true)
                .message("DELETE_BLOG_SUCCESS")
                .build();
    }
}
