package com.app.toeic.crawl.model;


import jakarta.persistence.*;
import lombok.*;
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
public class JobCrawl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;
    private String jobLink;
    @Column(length = 50)
    private String jobStatus;

    @CreationTimestamp
    private LocalDateTime startTime;

    @UpdateTimestamp
    private LocalDateTime endTime;
}
