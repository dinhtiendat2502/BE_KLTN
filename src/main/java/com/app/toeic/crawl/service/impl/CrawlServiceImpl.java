package com.app.toeic.crawl.service.impl;

import com.app.toeic.crawl.model.JobCrawl;
import com.app.toeic.crawl.repo.JobCrawlRepository;
import com.app.toeic.crawl.service.CrawlService;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import com.app.toeic.question.model.QuestionImage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CrawlServiceImpl implements CrawlService {
    private final JobCrawlRepository jobCrawlRepository;
    private final IExamRepository examRepository;
    public Set<Question> mCrawlPart12(Element element1, boolean element2, Part part) {
        var questionList = new ArrayList<Question>();
        int totalQuestion = element2 ? 6 : 25;
        for (int i = 1; i <= totalQuestion; i++) {
            questionList.add(Question.builder().part(part).questionHaveTranscript(true).build());
        }
        if (element2) {
            var listImage = element1.getElementsByClass("lazyel");
            for (int i = 0; i < listImage.size(); i++) {
                questionList.get(i).setQuestionImage(listImage.get(i).absUrl("data-src"));
            }
        }
        var listQuestionNumber = element1.getElementsByClass("question-number");
        for (int i = 0; i < listQuestionNumber.size(); i++) {
            questionList.get(i).setQuestionNumber(Integer.parseInt(listQuestionNumber.get(i).text()));
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
        getTranscriptExplanation(element1, questionList);
        return new HashSet<>(questionList);
    }

    private void getTranscriptExplanation(Element element1, ArrayList<Question> questionList) {
        var questionExplain = element1.getElementsByClass("question-explanation-wrapper");
        for (int i = 0; i < questionExplain.size(); i++) {
            questionList.get(i).setTranslateTranscript(questionExplain.get(i).getElementsByClass("collapse").getFirst().removeAttr(
                    "id").html());
        }
    }

    public Set<Question> mCrawlPart34(Element element1, boolean element2, Part part) {
        int totalQuestion = element2 ? 39 : 30;
        var questionList = new ArrayList<Question>();
        for (int i = 1; i <= totalQuestion; i++) {
            questionList.add(Question.builder().part(part).build());
        }
        var questionGroupWrapper = element1.getElementsByClass("question-group-wrapper");
        for (int i = 0; i < questionGroupWrapper.size(); i++) {
            var transcript = questionGroupWrapper.get(i).getElementsByClass("context-transcript").getFirst();
            var questionImage = questionGroupWrapper.get(i).getElementsByTag("img");
            transcript.getElementsByTag("a").remove();
            var numberQuestionInGroup = 3;
            var indexStart = i * numberQuestionInGroup;
            questionList.get(indexStart).setTranscript(transcript.html());
            if(CollectionUtils.isNotEmpty(questionImage)) {
                questionList.get(indexStart).setQuestionImage(questionImage.getFirst().absUrl("src"));
            }
            for (int j = 0; j < numberQuestionInGroup; j++) {
                var hasTranscript = (indexStart + j) == indexStart;
                questionList.get(indexStart + j).setQuestionHaveTranscript(hasTranscript);
                var questionContent = questionGroupWrapper.get(i).getElementsByClass("question-wrapper").get(j);
                var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
                getAnswerQuestion(questionList, indexStart + j, Integer.parseInt(questionNumber), questionContent);
            }
        }
        getTranscriptExplanation(element1, questionList);
        return new HashSet<>(questionList);
    }

    public Set<Question> mCrawlPart5(Element element, Part part) {
        int totalElement = 30;
        var questionList = new ArrayList<Question>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(Question.builder().part(part).build());
        }
        var questionWrapper = element.getElementsByClass("question-wrapper");
        for (int i = 0; i < questionWrapper.size(); i++) {
            var questionContent = questionWrapper.get(i);
            var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
            getAnswerQuestion(questionList, i, Integer.parseInt(questionNumber), questionContent);
        }
        getTranscriptExplanation(element, questionList);
        return new HashSet<>(questionList);
    }

    private void getAnswerQuestion(
            ArrayList<Question> questionList,
            int i,
            Integer questionNumber,
            Element questionContent
    ) {
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

    public Set<Question> mCrawlPart6(Element element, Part part) {
        int totalElement = 16;
        var questionList = new ArrayList<Question>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(Question.builder().part(part).build());
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
                questionList.get(indexStart + j).setQuestionNumber(Integer.parseInt(questionNumber));
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
        return new HashSet<>(questionList);
    }

    public Set<Question> mCrawlPart7(Element element, Part part) {
        int totalElement = 54;
        var questionList = new ArrayList<Question>();
        for (int i = 1; i <= totalElement; i++) {
            questionList.add(Question.builder().part(part).build());
        }
        var index = 0;
        var questionTwoCols = element.getElementsByClass("question-twocols");
        for (Element questionGroup : questionTwoCols) {
            var listQuestion = questionGroup.getElementsByClass("question-wrapper");
            var lisTranscript = questionGroup.getElementsByClass("question-explanation-wrapper");
            var listImage = questionGroup.getElementsByTag("img");
            var indexQuestion = 0;
            questionList.get(index).setHaveMultiImage(true);
            for (Element value : listImage) {
                questionList
                        .get(index)
                        .getQuestionImages()
                        .add(QuestionImage.builder().question(questionList.get(index)).questionImage(value.absUrl("src")).build());
            }
            questionList.get(index).setQuestionHaveTranscript(true);
            var transcript = questionGroup.getElementsByClass("context-transcript").getFirst();
            questionList.get(index).setTranscript(transcript.getElementsByClass("collapse").removeAttr("id").html());

            for (Element questionContent : listQuestion) {
                var questionNumber = questionContent.getElementsByTag("strong").getFirst().text();
                getAnswerQuestion(questionList, index, Integer.parseInt(questionNumber), questionContent);
                var questionExplain = lisTranscript.get(indexQuestion);
                questionList.get(index).setTranslateTranscript(questionExplain.getElementsByClass("collapse").getFirst().removeAttr(
                        "id").html());
                index++;
            }
        }
        return new HashSet<>(questionList);
    }

    @Override
    @Async("crawlDataExecutor")
    public void crawlData(Elements listPartContent, Document doc, JobCrawl job, Exam exam) {
        for (int i = 1; i <= listPartContent.size(); i++) {
            var part = Part.builder().partCode(STR."PART\{i}").partName(STR."Part \{i}").build();
            switch (i) {
                case 1 -> {
                    part.setNumberOfQuestion(6);
                    part.setQuestions(this.mCrawlPart12(listPartContent.getFirst(), true, part));
                }
                case 2 -> {
                    part.setNumberOfQuestion(25);
                    part.setQuestions(this.mCrawlPart12(listPartContent.get(1), false, part));
                }
                case 3 -> {
                    part.setNumberOfQuestion(39);
                    part.setQuestions(this.mCrawlPart34(listPartContent.get(2), true, part));
                }
                case 4 -> {
                    part.setNumberOfQuestion(30);
                    part.setQuestions(this.mCrawlPart34(listPartContent.get(3), false, part));
                }
                case 5 -> {
                    part.setNumberOfQuestion(30);
                    part.setQuestions(this.mCrawlPart5(listPartContent.get(4), part));
                }
                case 6 -> {
                    part.setNumberOfQuestion(16);
                    part.setQuestions(this.mCrawlPart6(listPartContent.get(5), part));
                }
                case 7 -> {
                    part.setNumberOfQuestion(54);
                    part.setQuestions(this.mCrawlPart7(listPartContent.get(6), part));
                }
            }
            part.setExam(exam);
            exam.getParts().add(part);
        }
        examRepository.save(exam);

        job.setJobStatus("DONE");
        jobCrawlRepository.save(job);
    }
}
