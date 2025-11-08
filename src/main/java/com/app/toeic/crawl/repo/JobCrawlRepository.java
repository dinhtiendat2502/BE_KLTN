package com.app.toeic.crawl.repo;

import com.app.toeic.crawl.model.JobCrawl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobCrawlRepository extends JpaRepository<JobCrawl, Long> {

    @Query("SELECT j FROM JobCrawl j WHERE j.jobLink like %?1%")
    List<JobCrawl> findByJobLink(String jobLink);

    Page<JobCrawl> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<JobCrawl> findAllByJobStatusAndStartTimeBetween(String jobStatus, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
