package com.app.toeic.external.service;

import com.app.toeic.question.model.Question;
import org.jsoup.nodes.Element;

import java.util.Set;

public interface CrawlService {
    Set<Question> crawlPart12(Element element1, boolean element2);

    Set<Question> crawlPart34(Element element1, boolean element2);

    Set<Question> crawlPart5(Element element);

    Set<Question> crawlPart6(Element element);

    Set<Question> crawlPart7(Element element);
}
