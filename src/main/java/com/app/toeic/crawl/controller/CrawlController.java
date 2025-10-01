package com.app.toeic.crawl.controller;


import com.app.toeic.crawl.model.CrawlConfig;
import com.app.toeic.crawl.model.JobCrawl;
import com.app.toeic.crawl.payload.CrawlDTO;
import com.app.toeic.crawl.repo.CrawlConfigRepository;
import com.app.toeic.crawl.repo.JobCrawlRepository;
import com.app.toeic.crawl.service.CrawlService;
import com.app.toeic.exam.model.Exam;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/crawl")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CrawlController {
    CrawlConfigRepository crawlConfigRepository;
    CrawlService crawlService;
    RestTemplate restTemplate;
    JobCrawlRepository jobCrawlRepository;

    @GetMapping("/test")
    public Object test() {
        String url = "http://localhost:8080/api/admin/crawl/get-data";
        var body = CrawlDTO.builder().url("https://study4.com/tests/4692/results/13263452/details/").build();
        return restTemplate.postForObject(url, body, Object.class);
    }

    @PostMapping("is-crawl")
    public Object isCrawl(@RequestBody CrawlDTO crawl) {
        String pattern = "(https://study4\\.com/tests/\\d+/results).*";
        String desiredUrl = crawl.getUrl().replaceAll(pattern, "$1");
        return "OK";
    }

    @PostMapping("get-data")
    public Object crawl(@RequestBody CrawlDTO crawl) throws IOException {
        String regex = "^https:\\/\\/study4\\.com\\/tests\\/(\\d+)\\/results\\/(\\d+)\\/details\\/$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(crawl.getUrl()).matches()) {
            return "URL_NOT_MATCH";
        }
        var connection = Jsoup.connect(crawl.getUrl());
        var config = crawlConfigRepository.findByEmail(crawl.getEmail()).orElse(CrawlConfig.builder().token(
                "csrftoken=taoxMpbf7FKoFHoZGyNqfdoKcwD0elEJgIKsrel28Pc7cEKWiO5XB9Tc8Lw4r0ge; sessionid=11wlkenrvfpsrris51vl9aqbixlvcvfq; cf_clearance=.UxZwpQCxxLgA3nOPuc8.XCIZ3pXEIYNPBVDEK1r_r8-1711956447-1.0.1.1-MTG7UGOio0vwN9itKBMXt3e8nMGDHKmXBgnQfft.DVHqFqok0rtyy.QQ9imW79wiElSwe8jX0eKt8vHd9HyijQ").build());
        connection.header("Cookie", config.getToken());
        var doc = connection.userAgent(config.getAgentUser()).get();
        var listPartContent = doc.getElementsByClass("test-questions-wrapper");
        int totalPart = 7;
        if (CollectionUtils.isNotEmpty(listPartContent) && listPartContent.size() != totalPart) {
            return "NOT_FULL_TEST";
        }
        var job = JobCrawl
                .builder()
                .jobLink(crawl.getUrl())
                .jobName("CRAWL_EXAM")
                .jobStatus("IN_PROGRESS");
        var exam = Exam.builder().status("ACTIVE").price(0.0).numberOfUserDoExam(0);
        var title = doc.getElementsByTag("h1").first();
        if (title != null && CollectionUtils.isNotEmpty(title.children())) {
            title.children().remove();
            var examTitleName = title.text().replace("Đáp án chi tiết: ", "");
            exam.examName(examTitleName);
            job.examName(examTitleName);
        }
        var rsJob = jobCrawlRepository.save(job.build());
        var audio = doc.getElementsByClass("post-audio-item").first();
        if (audio != null) {
            exam.examAudio(audio.child(0).absUrl("src"));
        }
        crawlService.crawlData(listPartContent, doc, rsJob, exam.build());
        return "OK";
    }
}
