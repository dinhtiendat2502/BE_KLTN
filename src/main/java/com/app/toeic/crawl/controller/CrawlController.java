package com.app.toeic.crawl.controller;


import com.app.toeic.crawl.model.CrawlConfig;
import com.app.toeic.crawl.model.JobCrawl;
import com.app.toeic.crawl.payload.CrawlConfigDTO;
import com.app.toeic.crawl.payload.CrawlDTO;
import com.app.toeic.crawl.repo.CrawlConfigRepository;
import com.app.toeic.crawl.repo.JobCrawlRepository;
import com.app.toeic.crawl.service.CrawlService;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.external.response.ResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/crawl")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CrawlController {
    CrawlConfigRepository crawlConfigRepository;
    CrawlService crawlService;
    JobCrawlRepository jobCrawlRepository;

    @GetMapping("all-config")
    public Object getAllConfig() {
        return crawlConfigRepository.findAll();
    }

    @PostMapping("update-config")
    public Object addConfig(@RequestBody CrawlConfigDTO config) {
        String[] msg = new String[1];
        crawlConfigRepository
                .findByEmail(config.getEmail())
                .ifPresentOrElse(crawlConfig -> {
                    crawlConfig.setToken(config.getToken());
                    crawlConfigRepository.save(crawlConfig);
                    msg[0] = "UPDATE_CONFIG_SUCCESS";
                }, () -> {
                    crawlConfigRepository
                            .save(CrawlConfig
                                    .builder()
                                    .token(config.getToken())
                                    .email(config.getEmail())
                                    .build());
                    msg[0] = "ADD_CONFIG_SUCCESS";
                });
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message(msg[0])
                .build();
    }

    @DeleteMapping("remove-config/{id}")
    public Object removeConfig(@PathVariable Integer id) {
        crawlConfigRepository.deleteById(id);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("REMOVE_CONFIG_SUCCESS")
                .build();
    }

    @DeleteMapping("remove-config/{email}")
    public Object removeConfig(@PathVariable String email) {
        crawlConfigRepository.deleteByEmail(email);
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("REMOVE_CONFIG_SUCCESS")
                .build();
    }

    @PostMapping("is-crawl")
    public Object isCrawl(@RequestBody CrawlDTO crawl) {
        String pattern = "(https://study4\\.com/tests/\\d+/results).*";
        String desiredUrl = crawl.getUrl().replaceAll(pattern, "$1");
        if (jobCrawlRepository.existsByJobLink(desiredUrl)) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("IS_CRAWL")
                    .build();
        }
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("NOT_CRAWL")
                .build();
    }

    @PostMapping("get-data")
    public Object crawl(@RequestBody CrawlDTO crawl) throws IOException {
        String regex = "^https:\\/\\/study4\\.com\\/tests\\/(\\d+)\\/results\\/(\\d+)\\/details\\/$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(crawl.getUrl()).matches()) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("URL_NOT_MATCH")
                    .build();
        }
        var connection = Jsoup.connect(crawl.getUrl());
        var config = crawlConfigRepository.findByEmail(crawl.getEmail()).orElse(CrawlConfig.builder().token(
                "csrftoken=taoxMpbf7FKoFHoZGyNqfdoKcwD0elEJgIKsrel28Pc7cEKWiO5XB9Tc8Lw4r0ge; sessionid=11wlkenrvfpsrris51vl9aqbixlvcvfq; cf_clearance=.UxZwpQCxxLgA3nOPuc8.XCIZ3pXEIYNPBVDEK1r_r8-1711956447-1.0.1.1-MTG7UGOio0vwN9itKBMXt3e8nMGDHKmXBgnQfft.DVHqFqok0rtyy.QQ9imW79wiElSwe8jX0eKt8vHd9HyijQ").build());
        connection.header("Cookie", config.getToken());
        var doc = connection.userAgent(config.getAgentUser()).get();
        var listPartContent = doc.getElementsByClass("test-questions-wrapper");
        int totalPart = 7;
        if (CollectionUtils.isNotEmpty(listPartContent) && listPartContent.size() != totalPart) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("PART_NOT_MATCH")
                    .build();
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
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CRAWL_SUCCESS")
                .build();
    }
}
