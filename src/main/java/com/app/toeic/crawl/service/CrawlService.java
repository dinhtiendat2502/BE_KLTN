package com.app.toeic.crawl.service;

import com.app.toeic.crawl.model.JobCrawl;
import com.app.toeic.exam.model.Exam;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public interface CrawlService {
    void crawlData(Elements elements, Document doc, JobCrawl job, Exam exam);
}
