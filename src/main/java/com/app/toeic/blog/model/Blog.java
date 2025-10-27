package com.app.toeic.blog.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long blogId;

    @Column(name = "title")
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @Column(name = "image")
    String thumbnail;

    String author;

    @Column(columnDefinition = "TINYTEXT")
    String summary;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
