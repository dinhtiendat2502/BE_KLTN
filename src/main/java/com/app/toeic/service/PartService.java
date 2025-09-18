package com.app.toeic.service;


import com.app.toeic.model.Exam;
import com.app.toeic.model.Part;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PartService {
    void init7PartForExam(Exam exam);

    List<Part> getAllPartByExamId(Integer examId);

    Part getPartById(Integer partId);

    void savePart(Part part);
}
