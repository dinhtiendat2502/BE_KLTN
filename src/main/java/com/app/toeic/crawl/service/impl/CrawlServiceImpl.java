package com.app.toeic.crawl.service.impl;

import com.app.toeic.crawl.response.QuestionResponse;
import com.app.toeic.crawl.service.CrawlService;
import com.app.toeic.question.model.Question;
import com.app.toeic.question.model.QuestionImage;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
                            .getFirst()
                            .text());
                    var firstSibling = element.firstElementSibling();
                    firstSibling
                            .getElementsByClass("context-transcript")
                            .forEach(eleScript -> eleScript
                                    .getElementsByTag("a")
                                    .remove());
                    question.translateTranscript(firstSibling
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
                                .forEach(img -> imageCrawl.add(QuestionImage
                                        .builder()
                                        .questionImage(img.absUrl("src"))
                                        .build()));
                        question.questionImages(imageCrawl);
                    }
                    questionList.add(question.build());
                });
        return questionList;
    }

    @Override
    public Set<Question> crawlPart34(Element element1, boolean element2) {
        element1
                .getElementsByClass("question-group-wrapper")
                .forEach(element -> {
                    var transcript = element.getElementsByClass("context-transcript");
                    transcript.forEach(ele -> ele
                            .getElementsByTag("a")
                            .remove());

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

    @Override
    public List<QuestionResponse> mCrawlPart12(Element element1, boolean element2) {
        var questionList = new ArrayList<QuestionResponse>();
        int totalQuestion = element2 ? 6 : 25;
        for (int i = 1; i <= totalQuestion; i++) {
            var question = QuestionResponse.builder();
            question.questionNumber(i + "").questionHaveTranscript(true);
            questionList.add(question.build());
        }
        if (element2) {
            var listImage = element1.getElementsByClass("lazyel");
            for (int i = 0; i < listImage.size(); i++) {
                questionList.get(i).setQuestionImage(listImage.get(i).absUrl("data-src"));
            }
        }
        var listQuestionNumber = element1.getElementsByClass("question-number");
        for (int i = 0; i < listQuestionNumber.size(); i++) {
            questionList.get(i).setQuestionNumber(listQuestionNumber.get(i).text());
        }
        var listContextTranscript = element1.getElementsByClass("context-transcript");
        for (int i = 0; i < listContextTranscript.size(); i++) {
            questionList.get(i).setTranscript(listContextTranscript.get(i).getElementsByClass("collapse").html());
        }
        var listQuestionAnswer = element1.getElementsByClass("question-answers");

        for (int i = 0; i < listQuestionAnswer.size(); i++) {
            var listAnswer = listQuestionAnswer.get(i).getElementsByClass("form-check-input");
            questionList.get(i).setAnswerA(listAnswer.getFirst().val());
            questionList.get(i).setAnswerB(listAnswer.get(1).val());
            questionList.get(i).setAnswerC(listAnswer.get(2).val());
            if (listAnswer.size() == 4) {
                questionList.get(i).setAnswerD(listAnswer.get(3).val());
            }
            var correctAnswer = listAnswer.stream().filter(aws -> aws.hasClass("correct")).findFirst();
            if (correctAnswer.isPresent()) {
                questionList.get(i).setCorrectAnswer(correctAnswer.get().val());
            } else {
                var otherCorrectAnswerElement = listQuestionAnswer.get(i).nextElementSibling();
                if (otherCorrectAnswerElement != null) {
                    questionList.get(i).setCorrectAnswer(otherCorrectAnswerElement.text().replace("Đáp án đúng:", ""));
                } else {
                    questionList.get(i).setCorrectAnswer("A");
                }
            }
        }
        var questionExplain = element1.getElementsByClass("question-explanation-wrapper");
        for (int i = 0; i < questionExplain.size(); i++) {
            questionList.get(i).setTranslateTranscript(questionExplain.get(i).getElementsByClass("collapse").getFirst().removeAttr(
                    "id").html());
        }
        return questionList;
    }

    @Override
    public List<QuestionResponse> mCrawlPart34(Element element1, boolean element2) {
        int totalQuestion = element2 ? 39 : 30;
        var questionList = new ArrayList<QuestionResponse>();
        for (int i = 1; i <= totalQuestion; i++) {
            questionList.add(QuestionResponse.builder().build());
        }
        var questionGroupWrapper = element1.getElementsByClass("question-group-wrapper");
        for (int i = 0; i < questionGroupWrapper.size(); i++) {
            var transcript = questionGroupWrapper.get(i).getElementsByClass("context-transcript").getFirst();
            transcript.getElementsByTag("a").remove();
            var numberQuestionInGroup = 3;
            var indexStart = i * numberQuestionInGroup;
            questionList.get(indexStart).setTranscript(transcript.html());
            for (int j = 0; j < numberQuestionInGroup; j++) {
                var hasTranscript = (indexStart + j) == indexStart;
                questionList.get(indexStart + j).setQuestionHaveTranscript(hasTranscript);
                var questionContent = questionGroupWrapper.get(i).getElementsByClass("question-wrapper").get(j);
                var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
                questionList.get(indexStart + j).setQuestionNumber(questionNumber);
                questionList.get(indexStart + j).setQuestionContent(questionContent.getElementsByClass("question-text").getFirst().text());
                var listAnswer = questionContent.getElementsByClass("form-check-label");
                questionList.get(indexStart + j).setAnswerA(listAnswer.getFirst().text());
                questionList.get(indexStart + j).setAnswerB(listAnswer.get(1).text());
                questionList.get(indexStart + j).setAnswerC(listAnswer.get(2).text());
                questionList.get(indexStart + j).setAnswerD(listAnswer.get(3).text());

                var correctAnswer = listAnswer.stream().filter(aws -> aws.hasClass("correct")).findFirst();
                if (correctAnswer.isPresent()) {
                    questionList.get(indexStart + j).setCorrectAnswer(correctAnswer.get().val());
                } else {
                    var otherCorrectAnswerElement = questionContent.getElementsByClass("text-success").getFirst();
                    if (otherCorrectAnswerElement != null) {
                        questionList.get(indexStart + j).setCorrectAnswer(otherCorrectAnswerElement.text().replace(
                                "Đáp án đúng:",
                                ""
                        ));
                    } else {
                        questionList.get(indexStart + j).setCorrectAnswer("A");
                    }
                }
            }
        }
        var questionExplain = element1.getElementsByClass("question-explanation-wrapper");
        for (int i = 0; i < questionExplain.size(); i++) {
            questionList.get(i).setTranslateTranscript(questionExplain.get(i).getElementsByClass("collapse").getFirst().removeAttr(
                    "id").html());
        }
        return questionList;
    }

    @Override
    public List<QuestionResponse> mCrawlPart5(Element element) {
        int totalElement = 30;
        var questionList = new ArrayList<QuestionResponse>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(QuestionResponse.builder().build());
        }

        var questionWrapper = element.getElementsByClass("question-wrapper");
        for (int i = 0; i < questionWrapper.size(); i++) {
            var questionContent = questionWrapper.get(i);
            var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
            questionList.get(i).setQuestionNumber(questionNumber);
            questionList.get(i).setQuestionContent(questionContent.getElementsByClass("question-text").getFirst().text());
            var listAnswer = questionContent.getElementsByClass("form-check-label");
            questionList.get(i).setAnswerA(listAnswer.getFirst().text());
            questionList.get(i).setAnswerB(listAnswer.get(1).text());
            questionList.get(i).setAnswerC(listAnswer.get(2).text());
            questionList.get(i).setAnswerD(listAnswer.get(3).text());

            var correctAnswer = listAnswer.stream().filter(aws -> aws.hasClass("correct")).findFirst();
            if (correctAnswer.isPresent()) {
                questionList.get(i).setCorrectAnswer(correctAnswer.get().val());
            } else {
                var otherCorrectAnswerElement = questionContent.getElementsByClass("text-success").getFirst();
                if (otherCorrectAnswerElement != null) {
                    questionList.get(i).setCorrectAnswer(otherCorrectAnswerElement.text().replace(
                            "Đáp án đúng:",
                            ""
                    ));
                } else {
                    questionList.get(i).setCorrectAnswer("A");
                }
            }
        }
        var questionExplain = element.getElementsByClass("question-explanation-wrapper");
        for (int i = 0; i < questionExplain.size(); i++) {
            questionList.get(i).setTranslateTranscript(questionExplain.get(i).getElementsByClass("collapse").getFirst().removeAttr(
                    "id").html());
        }
        return questionList;
    }

    @Override
    public List<QuestionResponse> mCrawlPart6(Element element) {
        int totalElement = 16;
        var questionList = new ArrayList<QuestionResponse>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(QuestionResponse.builder().build());
        }
        var questionGroupWrapper = element.getElementsByClass("question-group-wrapper");
        for (int i = 0; i < questionGroupWrapper.size(); i++) {
            var numberQuestionInGroup = 4;
            var indexStart = i * numberQuestionInGroup;
            var questionImage = questionGroupWrapper.get(i).getElementsByTag("img").getFirst().absUrl("src");
            questionList.get(indexStart).setQuestionImage(questionImage);
            var transcript = questionGroupWrapper.get(i).getElementsByClass("context-transcript").getFirst();
            questionList.get(indexStart).setTranscript(transcript.getElementsByClass("collapse").removeAttr("id").html());
            questionList.get(indexStart).setQuestionHaveTranscript(true);
            var questionWrapper = questionGroupWrapper.get(i).getElementsByClass("question-wrapper");
            var questionExplain = questionGroupWrapper.get(i).getElementsByClass("question-explanation-wrapper");
            for (int j = 0; j < questionWrapper.size(); j++) {
                var questionContent = questionWrapper.get(j);
                var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
                questionList.get(indexStart + j).setQuestionNumber(questionNumber);
                var listAnswer = questionContent.getElementsByClass("form-check-label");
                questionList.get(indexStart + j).setAnswerA(listAnswer.getFirst().text());
                questionList.get(indexStart + j).setAnswerB(listAnswer.get(1).text());
                questionList.get(indexStart + j).setAnswerC(listAnswer.get(2).text());
                questionList.get(indexStart + j).setAnswerD(listAnswer.get(3).text());

                var correctAnswer = listAnswer.stream().filter(aws -> aws.hasClass("correct")).findFirst();
                if (correctAnswer.isPresent()) {
                    questionList.get(indexStart + j).setCorrectAnswer(correctAnswer.get().val());
                } else {
                    var otherCorrectAnswerElement = questionContent.getElementsByClass("text-success").getFirst();
                    if (otherCorrectAnswerElement != null) {
                        questionList.get(indexStart + j).setCorrectAnswer(otherCorrectAnswerElement.text().replace(
                                "Đáp án đúng:",
                                ""
                        ));
                    } else {
                        questionList.get(indexStart + j).setCorrectAnswer("A");
                    }
                }
                questionList.get(indexStart + j).setTranslateTranscript(questionExplain.get(j).getElementsByClass(
                        "collapse").getFirst().removeAttr(
                        "id").html());
            }
        }
        return questionList;
    }

    @Override
    public List<QuestionResponse> mCrawlPart7(Element element) {
        int totalElement = 54;
        var questionList = new ArrayList<QuestionResponse>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(QuestionResponse.builder().build());
        }
        var questionTwoCols = element.getElementsByClass("question-twocols");
        for (int i = 0; i < questionTwoCols.size(); i++) {
            var questionGroup = questionTwoCols.get(i);
            var listQuestion = questionGroup.getElementsByClass("question-wrapper");
            var lisTranscript = questionGroup.getElementsByClass("question-explanation-wrapper");
            var listImage = questionGroup.getElementsByTag("img");

            int totalQuestionInGroup = listQuestion.size();
            var indexStart = i * totalQuestionInGroup;
            questionList.get(indexStart).setHaveMultiImage(true);
            for (Element value : listImage) {
                questionList.get(indexStart).getQuestionImages().add(value.absUrl("src"));
            }
            questionList.get(indexStart).setQuestionHaveTranscript(true);
            var transcript = questionGroup.getElementsByClass("context-transcript").getFirst();
            questionList.get(indexStart).setTranscript(transcript.getElementsByClass("collapse").removeAttr("id").html());

            for (int j = 0; j < listQuestion.size(); j++) {
                var questionContent = listQuestion.get(j);
                var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
                questionList.get(indexStart + j).setQuestionNumber(questionNumber);
                questionList.get(indexStart + j).setQuestionContent(questionContent.getElementsByClass("question-text").getFirst().text());
                var listAnswer = questionContent.getElementsByClass("form-check-label");
                questionList.get(indexStart + j).setAnswerA(listAnswer.getFirst().text());
                questionList.get(indexStart + j).setAnswerB(listAnswer.get(1).text());
                questionList.get(indexStart + j).setAnswerC(listAnswer.get(2).text());
                questionList.get(indexStart + j).setAnswerD(listAnswer.get(3).text());

                var correctAnswer = listAnswer.stream().filter(aws -> aws.hasClass("correct")).findFirst();
                if (correctAnswer.isPresent()) {
                    questionList.get(indexStart + j).setCorrectAnswer(correctAnswer.get().val());
                } else {
                    var otherCorrectAnswerElement = questionContent.getElementsByClass("text-success").getFirst();
                    if (otherCorrectAnswerElement != null) {
                        questionList.get(indexStart + j).setCorrectAnswer(otherCorrectAnswerElement.text().replace(
                                "Đáp án đúng:",
                                ""
                        ));
                    } else {
                        questionList.get(indexStart + j).setCorrectAnswer("A");
                    }
                }
                var questionExplain = lisTranscript.get(indexStart + j);
                questionList.get(indexStart + j).setTranslateTranscript(questionExplain.getElementsByClass("collapse").getFirst().removeAttr(
                        "id").html());
            }
        }
        return questionList;
    }
}
