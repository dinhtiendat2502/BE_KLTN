package com.app.toeic.crawl.service.impl;

import com.app.toeic.crawl.model.CrawlConfig;
import com.app.toeic.crawl.model.JobCrawl;
import com.app.toeic.crawl.repo.JobCrawlRepository;
import com.app.toeic.crawl.service.CrawlService;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.part.model.Part;
import com.app.toeic.question.model.Question;
import com.app.toeic.question.model.QuestionImage;
import com.app.toeic.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CrawlServiceImpl implements CrawlService {
    JobCrawlRepository jobCrawlRepository;
    IExamRepository examRepository;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public Set<Question> mCrawlPart12(Element element1, boolean element2, Part part) {
        var questionList = new ArrayList<Question>();
        int totalQuestion = element2 ? Constant.NUMBER_QUESTION_PART_1 : Constant.NUMBER_QUESTION_PART_2;
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
            if (listAnswer.size() == Constant.QUESTION_FOUR_ANSWER) {
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
        int totalQuestion = element2 ? Constant.NUMBER_QUESTION_PART_3 : Constant.NUMBER_QUESTION_PART_4;
        var questionList = new ArrayList<Question>();
        for (int i = 1; i <= totalQuestion; i++) {
            questionList.add(Question.builder().part(part).build());
        }
        var questionGroupWrapper = element1.getElementsByClass("question-group-wrapper");
        for (int i = 0; i < questionGroupWrapper.size(); i++) {
            var transcript = questionGroupWrapper.get(i).getElementsByClass("context-transcript").getFirst();
            var questionImage = questionGroupWrapper.get(i).getElementsByTag("img");
            transcript.getElementsByTag("a").remove();
            var numberQuestionInGroup = Constant.THREE_QUESTION_IN_GROUP;
            var indexStart = i * numberQuestionInGroup;
            questionList.get(indexStart).setTranscript(transcript.html());
            questionList.get(indexStart).setNumberQuestionInGroup(numberQuestionInGroup);
            if (CollectionUtils.isNotEmpty(questionImage)) {
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
            var numberQuestionInGroup = Constant.FOUR_QUESTION_IN_GROUP;
            var indexStart = i * numberQuestionInGroup;
            var questionImage = questionGroupWrapper.get(i).getElementsByTag("img").getFirst().absUrl("src");
            questionList.get(indexStart).setQuestionImage(questionImage);
            var transcript = questionGroupWrapper.get(i).getElementsByClass("context-transcript").getFirst();
            questionList.get(indexStart).setTranscript(transcript.getElementsByClass("collapse").removeAttr("id").html());
            questionList.get(indexStart).setQuestionHaveTranscript(true);
            var questionWrapper = questionGroupWrapper.get(i).getElementsByClass("question-wrapper");
            var questionExplain = questionGroupWrapper.get(i).getElementsByClass("question-explanation-wrapper");
            questionList.get(indexStart).setNumberQuestionInGroup(questionWrapper.size());
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
                        questionList
                                .get(indexStart + j)
                                .setCorrectAnswer(otherCorrectAnswerElement
                                        .text()
                                        .replace("Đáp án đúng:", "")
                                );
                    } else {
                        questionList.get(indexStart + j).setCorrectAnswer("A");
                    }
                }
                questionList
                        .get(indexStart + j)
                        .setTranslateTranscript(questionExplain
                                .get(j)
                                .getElementsByClass("collapse")
                                .getFirst()
                                .removeAttr("id")
                                .html()
                        );
            }
        }
        return new HashSet<>(questionList);
    }

    public Set<Question> mCrawlPart7(Element element, Part part) {
        int totalElement = Constant.NUMBER_QUESTION_PART_7;
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
            questionList.get(index).setNumberQuestionInGroup(listQuestion.size());
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
        listPartContent.parallelStream().forEach(partElement -> executorService.submit(() -> {
            var part = createPart(listPartContent.indexOf(partElement) + 1, partElement, exam);
            exam.getParts().add(part);
        }));
        examRepository.save(exam);
        job.setJobStatus("DONE");
        jobCrawlRepository.save(job);
    }

    @Override
    @Async("crawlDataExecutor")
    public void crawlDataV2(String url, CrawlConfig config, JobCrawl job) {
        var startTime = System.currentTimeMillis();
        try {
            var connection = Jsoup.connect(url);
            connection.header("Cookie", config.getToken());
            var doc = connection.userAgent(config.getAgentUser()).get();
            var listPartContent = doc.getElementsByClass("test-questions-wrapper");
            int totalPart = 7;
            if (CollectionUtils.isEmpty(listPartContent) || listPartContent.size() != totalPart) {
                job.setDescription("PART_NOT_MATCH");
                job.setJobStatus("FAILED");
                jobCrawlRepository.save(job);
                return;
            }
            var exam = Exam.builder().status("ACTIVE").price(0.0).numberOfUserDoExam(0);
            var title = doc.getElementsByTag("h1").first();
            if (title != null && CollectionUtils.isNotEmpty(title.children())) {
                title.children().remove();
                var examTitleName = title.text().replace("Đáp án chi tiết: ", "");
                exam.examName(examTitleName);
                job.setExamName(examTitleName);
            } else {
                job.setDescription("NOT_FOUND_EXAM_TO_CRAWL");
                job.setJobStatus("FAILED");
                jobCrawlRepository.save(job);
                return;
            }
            var audio = doc.getElementsByClass("post-audio-item").first();
            if (audio != null) {
                exam.examAudio(audio.child(0).absUrl("src"));
            }
            crawlData(listPartContent, doc, job, exam.build());
        } catch (IOException ex) {
            job.setDescription("CRAWL_FAILED");
            job.setJobStatus("FAILED");
            jobCrawlRepository.save(job);
            log.log(Level.SEVERE, "CrawlServiceImpl >> crawlDataV2 >> Error: {}", ex);
        } finally {
            log.info(MessageFormat.format(
                    "CrawlServiceImpl >> crawlDataV2 >> took: {0} ms",
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private Part createPart(int i, Element partElement, Exam exam) {
        var part = Part.builder().partCode("PART%d".formatted(i)).partName("Part %d".formatted(i)).build();
        switch (i) {
            case 1 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_1);
                part.setPartContent(Constant.PART1_CONTENT);
                part.setQuestions(this.mCrawlPart12(partElement, true, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 1 >> status: IN_PROGRESS");
            }
            case 2 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_2);
                part.setPartContent(Constant.PART2_CONTENT);
                part.setQuestions(this.mCrawlPart12(partElement, false, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 2 >> status: IN_PROGRESS");
            }
            case 3 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_3);
                part.setPartContent(Constant.PART3_CONTENT);
                part.setQuestions(this.mCrawlPart34(partElement, true, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 3 >> status: IN_PROGRESS");
            }
            case 4 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_4);
                part.setPartContent(Constant.PART4_CONTENT);
                part.setQuestions(this.mCrawlPart34(partElement, false, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 4 >> status: IN_PROGRESS");
            }
            case 5 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_5);
                part.setPartContent(Constant.PART5_CONTENT);
                part.setQuestions(this.mCrawlPart5(partElement, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 5 >> status: IN_PROGRESS");
            }
            case 6 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_6);
                part.setPartContent(Constant.PART6_CONTENT);
                part.setQuestions(this.mCrawlPart6(partElement, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 6 >> status: IN_PROGRESS");
            }
            case 7 -> {
                part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_7);
                part.setPartContent(Constant.PART7_CONTENT);
                part.setQuestions(this.mCrawlPart7(partElement, part));
                log.log(Level.INFO, "CrawlServiceImpl >> crawlData >> Part 7 >> status: IN_PROGRESS");
            }
        }
        part.setExam(exam);
        return part;
    }
}
