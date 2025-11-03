package com.app.toeic.exam.controller;

import com.app.toeic.exam.model.RealExam;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.exam.repo.RealExamRepository;
import com.app.toeic.exam.response.PartResponse;
import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.firebase.service.impl.FirebaseStorageServiceImpl;
import com.app.toeic.part.repo.IPartRepository;
import com.app.toeic.util.DatetimeUtils;
import com.app.toeic.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;

@Log
@RequestMapping("/real-exam")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RealExamController {
    FirebaseStorageServiceImpl firebaseStorageService;
    RealExamRepository realExamRepository;
    IExamRepository examRepository;
    IPartRepository iPartRepository;

    @PostMapping("create")
    @SneakyThrows
    public Object createRealExam(
            @RequestParam("examName") String examName,
            @RequestParam(value = "img", required = false) MultipartFile img,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate
    ) {
        var realExam = RealExam
                .builder()
                .examName(examName)
                .fromDate(DatetimeUtils.getFromDate(fromDate))
                .toDate(DatetimeUtils.getToDate(toDate))
                .build();
        if (FileUtils.isNotEmpty(img)) {
            realExam.setExamImage(firebaseStorageService.uploadFile(img));
        }
        if (FileUtils.isNotEmpty(audio)) {
            realExam.setExamAudio(firebaseStorageService.uploadFile(audio));
        }
        realExamRepository.save(realExam);
        return ResponseVO
                .builder()
                .success(true)
                .message("CREATE_REAL_EXAM_SUCCESS")
                .build();
    }

    @PostMapping("random/{examId}")
    public Object randomRealExam(@PathVariable Long examId) {
        var realExam = realExamRepository.findById(examId).orElse(null);
        var success = false;
        var message = "REAL_EXAM_NOT_FOUND";
        var list = iPartRepository.findAllPartWithExamNotFree(false);
        list.forEach(part -> {
            log.info(MessageFormat.format("RealExamController >> randomRealExam >> {0}, {1}", part.getPartName(), part.getQuestions().size()));
        });
        // group by partCode
        var test = list
                .stream()
                .collect(java.util.stream.Collectors.groupingBy(PartResponse::getPartCode));
        return ResponseVO
                .builder()
                .success(success)
                .message(message)
                .data(test)
                .build();
    }
}
