package com.app.toeic.crawl.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_crawl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCrawl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String jobName;
    String examName;
    String jobLink;
    @Column(length = 50)
    String jobStatus;

    @CreationTimestamp
    LocalDateTime startTime;

    @UpdateTimestamp
    LocalDateTime endTime;
}
