package com.app.toeic.part.service;


import com.app.toeic.exam.model.Exam;
import com.app.toeic.part.model.Part;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public interface PartService {
    void init7PartForExam(Exam exam);

    List<Part> getAllPartByExamId(Integer examId);

    Part getPartById(Integer partId);

    void savePart(Part part);

    void exportPartToExcel(
            OutputStream outputStream,
            String[] headerMap,
            List<com.app.toeic.exam.response.PartResponse> parts
    ) throws IOException;
}
