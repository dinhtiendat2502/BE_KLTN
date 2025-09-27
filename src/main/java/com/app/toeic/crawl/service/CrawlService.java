package com.app.toeic.crawl.service;

import com.app.toeic.crawl.response.QuestionResponse;
import com.app.toeic.question.model.Question;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Set;

public interface CrawlService {
    Set<Question> crawlPart12(Element element1, boolean element2);

    Set<Question> crawlPart34(Element element1, boolean element2);

    Set<Question> crawlPart5(Element element);

    Set<Question> crawlPart6(Element element);

    Set<Question> crawlPart7(Element element);


    List<QuestionResponse> mCrawlPart12(Element element1, boolean element2);

    List<QuestionResponse> mCrawlPart34(Element element1, boolean element2);

    List<QuestionResponse> mCrawlPart5(Element element);

    List<QuestionResponse> mCrawlPart6(Element element);

    List<QuestionResponse> mCrawlPart7(Element element);
}
