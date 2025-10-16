package com.app.toeic.part.service.impl;


import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.part.model.Part;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.part.repo.IPartRepository;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PartServiceIpml implements PartService {
    IPartRepository partRepository;
    IExamRepository examRepository;

    @Override
    @Transactional
    public void init7PartForExam(Exam exam) {
        for (int i = 1; i <= 7; i++) {
            var part = Part
                    .builder()
                    .partName("Part " + i)
                    .partCode("PART" + i)
                    .status("ACTIVE")
                    .exam(exam)
                    .build();

            switch (i) {
                case 1 -> {
                    part.setPartAudio(exam.getAudioPart1());
                    part.setNumberOfQuestion(6);
                    part.setPartContent("""
                            Directions : For each question in this part, you will hear four statements about a picture in your test book. When you hear the statements, you must select the one statement that best describes what you see in the picture. Then find the number of the question on your answer sheet and mark your answer. The statements will not be printed in your test book and will be spoken only one time.
                            """);
                }
                case 2 -> {
                    part.setPartAudio(exam.getAudioPart2());
                    part.setNumberOfQuestion(25);
                    part.setPartContent("""
                            Directions : You will hear a question or statement and three responses spoken in English. They will not be printed in your test book and will be spoken only one time. Select the best response to the question or statement and mark the letter (A), (B), or (C) on your answer sheet.
                            """);
                }
                case 3 -> {
                    part.setPartAudio(exam.getAudioPart3());
                    part.setNumberOfQuestion(39);
                    part.setPartContent("""
                            Directions : You will hear some conversations between two or more people. You will be asked to answer three questions about what the speakers say in each conversation. Select the best response to each question and mark the letter (A), (B), (C), or (D) on your answer sheet. The conversations will not be printed in your test book and will be spoken only one time.
                            """);
                }
                case 4 -> {
                    part.setPartAudio(exam.getAudioPart4());
                    part.setNumberOfQuestion(30);
                    part.setPartContent("""
                            Directions : You will hear some talks given by a single speaker. You will be asked to answer three questions about what the speaker says in each talk. Select the best response to each question and mark the letter (A), (B), (C), or (D) on your answer sheet. The talks will not be printed in your test book and will be spoken only one time.
                            """);
                }
                case 5 -> {
                    part.setNumberOfQuestion(30);
                    part.setPartContent("""
                            Directions: A word or phrase is missing in each of the sentences below. Four answer choices are given below each sentence. Select the best answer to complete the sentence. Then mark the letter (A), (B), (C), or (D) on your answer sheet.
                            """);
                }
                case 6 -> {
                    part.setNumberOfQuestion(16);
                    part.setPartContent("""
                            Directions : Read the texts that follow. A word, phrase, or sentence is missing in parts of each text. Four answer choices for each question are given below the text. Select the best answer to complete the text. Then mark the letter (A), (B), (C), or (D) on your answer sheet.
                            """);
                }
                case 7 -> {
                    part.setPartContent("""
                            Directions: In this part you will read a selection of texts, such as magazine and newspaper articles, e-mails, and instant messages. Each text or set of texts is followed by several questions. Select the best answer for each question and mark the letter (A), (B), (C), or (D) on your answer sheet.
                            """);
                    part.setNumberOfQuestion(54);
                }
                default -> {
                    break;
                }
            }
            partRepository.save(part);
        }
    }

    @Override
    public List<Part> getAllPartByExamId(Integer examId) {
        var exam = examRepository
                .findById(examId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy đề thi"));
        return partRepository.findAllByExamWithQuestions(exam.getExamId());
    }

    @Override
    public Part getPartById(Integer partId) {
        return partRepository
                .findPartWithQuestion(partId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy phần thi"));
    }

    @Override
    @Transactional
    public void savePart(Part part) {
        partRepository.save(part);
    }
}
