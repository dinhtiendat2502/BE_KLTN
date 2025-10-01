package com.app.toeic.crawl.controller;


import com.app.toeic.crawl.model.CrawlConfig;
import com.app.toeic.crawl.payload.CrawlDTO;
import com.app.toeic.crawl.repo.CrawlConfigRepository;
import com.app.toeic.crawl.response.ExamResponse;
import com.app.toeic.crawl.response.PartResponse;
import com.app.toeic.crawl.response.QuestionResponse;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.part.model.Part;
import com.app.toeic.crawl.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/admin/crawl")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CrawlController {
    CrawlConfigRepository crawlConfigRepository;
    CrawlService crawlService;
    RestTemplate restTemplate;

    @GetMapping("/test")
    public Object test() {
        String url = "http://localhost:8080/api/admin/crawl/test-crawl";
        var body = CrawlDTO.builder().url("https://study4.com/tests/4692/results/13263452/details/").build();
        return restTemplate.postForObject(url, body, Object.class);
    }

    @PostMapping("test-crawl")
    public Object crawl(@RequestBody CrawlDTO crawl) throws IOException {
        String regex = "^https:\\/\\/study4\\.com\\/tests\\/(\\d+)\\/results\\/(\\d+)\\/details\\/$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(crawl.getUrl()).matches()) {
            return "URL_NOT_MATCH";
        }
        var connection = Jsoup.connect(crawl.getUrl());
        var config = crawlConfigRepository.findById(1).orElse(CrawlConfig.builder().token(
                "csrftoken=taoxMpbf7FKoFHoZGyNqfdoKcwD0elEJgIKsrel28Pc7cEKWiO5XB9Tc8Lw4r0ge; sessionid=11wlkenrvfpsrris51vl9aqbixlvcvfq; cf_clearance=.UxZwpQCxxLgA3nOPuc8.XCIZ3pXEIYNPBVDEK1r_r8-1711956447-1.0.1.1-MTG7UGOio0vwN9itKBMXt3e8nMGDHKmXBgnQfft.DVHqFqok0rtyy.QQ9imW79wiElSwe8jX0eKt8vHd9HyijQ").agentUser(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36").build());
        connection.header("Cookie", config.getToken());
        var doc = connection
                .userAgent(config.getAgentUser())
                .get();
        var listPartContent = doc.getElementsByClass("test-questions-wrapper");
        int totalPart = 7;
        if (CollectionUtils.isNotEmpty(listPartContent) && listPartContent.size() != totalPart) {
            return "NOT_FULL_TEST";
        }
        var exam = ExamResponse.builder().status("ACTIVE").price(0.0).numberOfUserDoExam(0);
        var title = doc.getElementsByTag("h1").first();
        if (title != null && CollectionUtils.isNotEmpty(title.children())) {
            title.children().remove();
            exam.examName(title.text().replace("Đáp án chi tiết: ", ""));
        }
        var audio = doc.getElementsByClass("post-audio-item").first();
        if (audio != null) {
            exam.examAudio(audio.child(0).absUrl("src"));
        }
        var listPart = new ArrayList<PartResponse>();
        for (int i = totalPart; i > 0; i--) {
            var part = PartResponse.builder().partCode("PART" + i).partName("Part " + i);
            switch (i) {
                case 1 -> {
                    part.numberOfQuestion(6);
                    part.questions(crawlService.mCrawlPart12(listPartContent.getFirst(), true));
                }
                case 2 -> {
                    part.numberOfQuestion(25);
                    part.questions(crawlService.mCrawlPart12(listPartContent.get(1), false));
                }
                case 3 -> {
                    part.numberOfQuestion(39);
                    part.questions(crawlService.mCrawlPart34(listPartContent.get(2), true));
                }
                case 4 -> {
                    part.numberOfQuestion(30);
                    part.questions(crawlService.mCrawlPart34(listPartContent.get(3), false));
                }
                case 5 -> {
                    part.numberOfQuestion(30);
                    part.questions(crawlService.mCrawlPart5(listPartContent.get(4)));
                }
                case 6 -> {
                    part.numberOfQuestion(16);
                    part.questions(crawlService.mCrawlPart6(listPartContent.get(5)));
                }
                case 7 -> {
                    part.numberOfQuestion(54);
                    part.questions(crawlService.mCrawlPart7(listPartContent.get(6)));
                }
            }
            listPart.add(part.build());
        }
        exam.parts(listPart);
        return exam.build();
    }

    @PostMapping("/get")
    public Object craw(@RequestBody CrawlDTO crawl) throws IOException {
        var connection = Jsoup.connect(crawl.getUrl());
        connection.header("Cookie", "");
        var doc = connection
                .userAgent(crawl.getAgent())
                .get();
        var examName = doc
                .getElementsByClass("h3 text-center")
                .text();
        var audioSrc = doc
                .getElementsByClass("post-audio-item")
                .getFirst()
                .child(0)
                .absUrl("src");
        var initPartList = IntStream
                .range(1, 8)
                .mapToObj(i -> Part
                        .builder()
                        .partCode("PART" + i)
                        .partName("Part " + i)
                        .status("ACTIVE")
                        .build())
                .sorted(Comparator.comparing(Part::getPartCode))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        var exam = Exam
                .builder()
                .examAudio(audioSrc)
                .status("ACTIVE")
                .numberOfUserDoExam(200)
                .price(0.0)
                .parts(initPartList)
                .examName(examName
                        .replace("Đáp án chi tiết:", "")
                        .trim())
                .build();

        var testQuestionsWrapper = doc.getElementsByClass("test-questions-wrapper");
        if (testQuestionsWrapper.size() != 7) {
            return "Không tìm thấy đề thi";
        }

        var part1 = crawlService.crawlPart12(testQuestionsWrapper.get(0), true);
        var part2 = crawlService.crawlPart12(testQuestionsWrapper.get(1), false);
        var part3 = crawlService.crawlPart34(testQuestionsWrapper.get(2), true);
        var part4 = crawlService.crawlPart34(testQuestionsWrapper.get(3), false);
        var part5 = crawlService.crawlPart5(testQuestionsWrapper.get(4));
        var part6 = crawlService.crawlPart6(testQuestionsWrapper.get(5));
        var part7 = crawlService.crawlPart7(testQuestionsWrapper.get(6));

        exam
                .getParts()
                .forEach(part -> {
                    switch (part.getPartCode()) {
                        case "PART1" -> part.setQuestions(part1);
                        case "PART2" -> part.setQuestions(part2);
                        case "PART3" -> part.setQuestions(part3);
                        case "PART4" -> part.setQuestions(part4);
                        case "PART5" -> part.setQuestions(part5);
                        case "PART6" -> part.setQuestions(part6);
                        case "PART7" -> part.setQuestions(part7);
                    }
                });
        return exam;
    }
}
