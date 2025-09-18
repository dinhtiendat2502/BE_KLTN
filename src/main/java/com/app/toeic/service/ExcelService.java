package com.app.toeic.service;

import com.app.toeic.model.Part;
import com.app.toeic.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    List<Question> excelToPart1(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart2(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart3(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart4(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart5(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart6(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;

    List<Question> excelToPart7(InputStream is, Part part, List<Question> list, Boolean isAddNew) throws IOException;
}
