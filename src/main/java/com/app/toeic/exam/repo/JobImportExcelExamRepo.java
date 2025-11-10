package com.app.toeic.exam.repo;

import com.app.toeic.exam.model.JobImportExcelExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobImportExcelExamRepo extends JpaRepository<JobImportExcelExam, Integer>{
}
