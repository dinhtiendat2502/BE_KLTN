package com.app.toeic.controller.admin;


import com.app.toeic.dto.CrawlDTO;
import com.app.toeic.model.Exam;
import com.app.toeic.model.Part;
import com.app.toeic.service.CrawlService;
import com.app.toeic.service.ProviderExternalService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/admin/crawl")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CrawlController {
    ProviderExternalService providerExternalService;
    CrawlService crawlService;

    @PostMapping("/get")
    public Object craw(@RequestBody CrawlDTO crawl) throws IOException {
        var connection = Jsoup.connect(crawl.getUrl());
        var provider = providerExternalService.getProviderExternalByCode(crawl.getProviderCode());
        connection.header("Cookie", provider.getCookie());
        var doc = connection
                .userAgent(crawl.getAgent())
                .get();
        var examName = doc
                .getElementsByClass("h3 text-center")
                .text();
        var audioSrc = doc
                .getElementsByClass("post-audio-item")
                .get(0)
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
                        default -> {
                        }
                    }
                });
        return exam;
    }
}
