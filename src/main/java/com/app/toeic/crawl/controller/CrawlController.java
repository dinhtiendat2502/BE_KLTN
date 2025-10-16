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
import com.app.toeic.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Log
@RestController
@RequestMapping("/admin/crawl")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

    @GetMapping("all-job")
    public Object getAllJob(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "status", defaultValue = "ALL") String status
    ) {
        if("ALL".equalsIgnoreCase(status)){
            return jobCrawlRepository.findAll(PageRequest.of(page, size));
        } else {
            return jobCrawlRepository.findAllByJobStatus(status, PageRequest.of(page, size));
        }
    }


    @PostMapping("is-crawl")
    public Object isCrawl(@RequestBody CrawlDTO crawl) {
        String pattern = "(https://study4\\.com/tests/\\d+/results).*";
        String desiredUrl = crawl.getUrl().replaceAll(pattern, "$1");
        var list = jobCrawlRepository.findByJobLink(desiredUrl);
        if (CollectionUtils.isNotEmpty(list)
         || !list.isEmpty()) {
            return ResponseVO
                    .builder()
                    .success(true)
                    .message("IS_CRAWL")
                    .build();
        }
        return ResponseVO
                .builder()
                .success(false)
                .message("NOT_CRAWL")
                .build();
    }

    @PostMapping("get-data")
    public Object crawl(@RequestBody CrawlDTO crawl) throws IOException {
        String regex = "^https://study4\\.com/tests/(\\d+)/results/(\\d+)/details/$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(crawl.getUrl()).matches()) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("URL_CRAWL_NOT_MATCH")
                    .build();
        }
        var connection = Jsoup.connect(crawl.getUrl());
        var config = crawlConfigRepository.findByEmail(crawl.getEmail()).orElse(null);
        if (config == null) {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("CRAWL_CONFIG_NOT_FOUND")
                    .build();
        }
        connection.header("Cookie", config.getToken());
        var doc = connection.userAgent(config.getAgentUser()).get();
        var listPartContent = doc.getElementsByClass("test-questions-wrapper");
        int totalPart = 7;
        if (CollectionUtils.isEmpty(listPartContent) || listPartContent.size() != totalPart) {
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
        } else {
            return ResponseVO
                    .builder()
                    .success(Boolean.FALSE)
                    .message("NOT_FOUND_EXAM_TO_CRAWL")
                    .build();
        }
        var rsJob = jobCrawlRepository.save(job.build());
        var audio = doc.getElementsByClass("post-audio-item").first();
        if (audio != null) {
            exam.examAudio(audio.child(0).absUrl("src"));
        }
        crawlService.crawlData(listPartContent, doc, rsJob, exam.build());
        log.log(Level.INFO, MessageFormat.format("CrawlController >> crawl >> body: {0}", JsonConverter.convertObjectToJson(crawl)));
        return ResponseVO
                .builder()
                .success(Boolean.TRUE)
                .message("CRAWL_SUCCESS")
                .build();
    }
}
