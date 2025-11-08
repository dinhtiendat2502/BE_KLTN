package com.app.toeic.exam.service.impl;

import com.app.toeic.exam.model.JobImportExcelExam;
import com.app.toeic.exception.AppException;
import com.app.toeic.exam.model.Exam;
import com.app.toeic.exam.repo.IExamRepository;
import com.app.toeic.exam.response.ExamVO;
import com.app.toeic.exam.service.ExamService;
import com.app.toeic.part.service.PartService;
import com.app.toeic.util.Constant;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ExamServiceImpl implements ExamService {
    IExamRepository examRepository;
    PartService partService;
    private static final List<String> LIST_PART_CODE = List.of(
            "PART1",
            "PART2",
            "PART3",
            "PART4",
            "PART5",
            "PART6",
            "PART7"
    );

    @Override
    public Object getAllExam() {
        return examRepository.findAllByStatus(Constant.STATUS_ACTIVE);
    }

    @Override
    public Object getAllExamForAdmin() {
        return examRepository.findAllByStatusForAdmin(Constant.STATUS_INACTIVE);
    }

    @Override
    public Object getAllRealExam() {
        return examRepository.findAllRealTest(Constant.STATUS_ACTIVE);
    }

    @Override
    public void increaseUserDoExam(Integer examId) {
        examRepository.findById(examId).ifPresent(exam -> {
            exam.setNumberOfUserDoExam(exam.getNumberOfUserDoExam() + 1);
            examRepository.save(exam);
        });
    }

    @Override
    @Transactional
    public void addExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), Integer.MIN_VALUE))) {
            throw new AppException(HttpStatus.SEE_OTHER, "EXAM_EXISTED");
        }
        var returnExam = examRepository.save(exam);
        partService.init7PartForExam(returnExam);
    }

    @Override
    @Transactional
    public void updateExam(Exam exam) {
        if (Boolean.TRUE.equals(examRepository.existsExamByExamName(exam.getExamName(), exam.getExamId()))) {
            throw new AppException(HttpStatus.SEE_OTHER, "EXAM_NAME_EXISTED");
        }
        examRepository.save(exam);
    }

    @Override
    public Object removeExam(Integer examId) {
        var exam = examRepository
                .findById(examId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "EXAM_NOT_FOUND"));
        exam.setStatus(Constant.STATUS_INACTIVE);
        examRepository.save(exam);
        return "DELETE_EXAM_SUCCESS";
    }

    @Override
    public Optional<Exam> findById(Integer examId) {
        return examRepository.findById(examId);
    }

    @Override
    public Optional<Exam> findExamWithPart(Integer examId) {
        return examRepository.findExamWithPart(examId);
    }

    @Override
    public Object getAllExamByTopic(Integer topicId) {
        if (topicId == null || topicId == 0) {
            return examRepository.findAllByStatus(Constant.STATUS_ACTIVE);
        }
        if (topicId == -1) {
            return examRepository.findAllByTopicIsNull();
        }
        return examRepository.findAllByStatusAndTopicId(topicId);
    }

    @Override
    public Optional<ExamVO.ExamList> findExamByExamId(Integer examId) {
        return examRepository.findExamByExamId(examId);
    }

    @Override
    public Optional<ExamVO.ExamFullQuestion> findExamWithFullQuestion(Integer examId) {
        return examRepository.findExamWithFullQuestion(examId);
    }

    @Override
    public Optional<ExamVO.ExamFullQuestionWithAnswer> findExamFullQuestionWithAnswer(Integer examId) {
        return examRepository.findExamFullQuestionWithAnswer(examId);
    }

    @Override
    public String findCorrectAnswer(ExamVO.ExamFullQuestionWithAnswer examFullQuestionWithAnswer, Integer questionId) {
        return examFullQuestionWithAnswer
                .getParts()
                .stream()
                .flatMap(part -> part
                        .getQuestions()
                        .stream())
                .filter(question -> Objects.equals(question.getQuestionId(), questionId))
                .map(ExamVO.ExamFullQuestionWithAnswer.Part.Question::getCorrectAnswer)
                .findFirst()
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "NOT_FOUND_QUESTION"));
    }

    @Override
    public Optional<ExamVO.ExamFullQuestion> findExamPractice(int i, List<String> listPart) {
        return examRepository.findExamPractice(i, listPart);
    }

    @Override
    public void save(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    @Async(Constant.TOEICUTE)
    public void importExcelExam(MultipartFile excel, JobImportExcelExam job, Exam exam) throws AppException {
        try {
            var workbook = new XSSFWorkbook(excel.getInputStream());
            LIST_PART_CODE.parallelStream().forEach(partCode -> {
                var sheet = workbook.getSheet(partCode);
                if (sheet != null) {
                    importPartExcel(sheet, partCode);
                }
            });
        } catch (Exception e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "UPLOAD_AUDIO_ERROR");
        }
    }

    private void importPartExcel(XSSFSheet sheet, String partCode) {

    }
}
