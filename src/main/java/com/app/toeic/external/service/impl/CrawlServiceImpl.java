package com.app.toeic.external.service.impl;

import com.app.toeic.external.service.CrawlService;
import com.app.toeic.question.model.Question;
import com.app.toeic.question.model.QuestionImage;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CrawlServiceImpl implements CrawlService {
    @Override
    public Set<Question> crawlPart12(Element element1, boolean element2) {
        Set<Question> questionList = new HashSet<>();
        element1
                .getElementsByClass("question-wrapper")
                .forEach(element -> {
                    var question = Question.builder();
                    question.questionNumber(element
                                                    .getElementsByTag("strong")
                                                    .get(0)
                                                    .text());
                    var firstSibling = element.firstElementSibling();
                    firstSibling
                            .getElementsByClass("context-transcript")
                            .forEach(eleScript -> {
                                eleScript
                                        .getElementsByTag("a")
                                        .remove();
                            });
                    question.transcriptAudio(firstSibling
                                                     .getElementsByClass("context-transcript")
                                                     .html());
                    var answerList = element.getElementsByClass("form-check-label");
                    question.answerA(answerList
                                             .get(0)
                                             .text());
                    question.answerB(answerList
                                             .get(1)
                                             .text());
                    question.answerC(answerList
                                             .get(2)
                                             .text());
                    if (answerList.size() == 4) {
                        question.answerD(answerList
                                                 .get(3)
                                                 .text());
                    }
                    var correctAnswer = element
                            .getElementsByClass("text-success")
                            .text();
                    question.correctAnswer(String.valueOf(correctAnswer.charAt(correctAnswer.length() - 1)));
                    if (element2) {
                        var imageCrawl = new HashSet<QuestionImage>();
                        firstSibling
                                .getElementsByTag("img")
                                .forEach(img -> {
                                    imageCrawl.add(QuestionImage
                                                           .builder()
                                                           .questionImage(img.absUrl("src"))
                                                           .build());
                                });
                        question.questionImages(imageCrawl);
                    }
                    questionList.add(question.build());
                });
        return questionList;
    }

    @Override
    public Set<Question> crawlPart34(Element element1, boolean element2) {
        Set<Question> questionList = new HashSet<>();
        element1
                .getElementsByClass("question-group-wrapper")
                .forEach(element -> {
                    var transcript = element.getElementsByClass("context-transcript");
                    transcript.forEach(ele -> {
                        ele
                                .getElementsByTag("a")
                                .remove();
                    });

                });
        return null;
    }

    @Override
    public Set<Question> crawlPart5(Element element) {
        return null;
    }

    @Override
    public Set<Question> crawlPart6(Element element) {
        return null;
    }

    @Override
    public Set<Question> crawlPart7(Element element) {
        return null;
    }
}
