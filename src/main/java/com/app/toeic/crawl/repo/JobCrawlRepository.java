package com.app.toeic.crawl.repo;

import com.app.toeic.crawl.model.JobCrawl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCrawlRepository extends JpaRepository<JobCrawl, Long>{
}
