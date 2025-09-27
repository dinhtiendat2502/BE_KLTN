package com.app.toeic.crawl.repo;

import com.app.toeic.crawl.model.CrawlConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlConfigRepository extends JpaRepository<CrawlConfig, Integer> {
}
