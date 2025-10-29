package com.app.toeic.part.service.impl;


import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.part.model.Part;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.part.repo.IPartRepository;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Log
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
                    .partName(MessageFormat.format("Part {0}", i))
                    .partCode(MessageFormat.format("PART{0}", i))
                    .status(Constant.STATUS_ACTIVE)
                    .exam(exam)
                    .build();

            switch (i) {
                case 1 -> {
                    part.setPartAudio(exam.getAudioPart1());
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_1);
                    part.setPartContent(Constant.PART1_CONTENT);
                }
                case 2 -> {
                    part.setPartAudio(exam.getAudioPart2());
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_2);
                    part.setPartContent(Constant.PART2_CONTENT);
                }
                case 3 -> {
                    part.setPartAudio(exam.getAudioPart3());
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_3);
                    part.setPartContent(Constant.PART3_CONTENT);
                }
                case 4 -> {
                    part.setPartAudio(exam.getAudioPart4());
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_4);
                    part.setPartContent(Constant.PART4_CONTENT);
                }
                case 5 -> {
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_5);
                    part.setPartContent(Constant.PART5_CONTENT);
                }
                case 6 -> {
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_6);
                    part.setPartContent(Constant.PART6_CONTENT);
                }
                case 7 -> {
                    part.setPartContent(Constant.PART7_CONTENT);
                    part.setNumberOfQuestion(Constant.NUMBER_QUESTION_PART_7);
                }
                default -> log.info("PartServiceIpml >> init7PartForExam >> Invalid part number");
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
