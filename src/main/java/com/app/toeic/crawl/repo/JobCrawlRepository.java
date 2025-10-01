package com.app.toeic.crawl.repo;

import com.app.toeic.crawl.model.JobCrawl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCrawlRepository extends JpaRepository<JobCrawl, Long>{

    @Query("SELECT j FROM JobCrawl j WHERE j.jobLink like %?1%")
    boolean existsByJobLink(String jobLink);
}
