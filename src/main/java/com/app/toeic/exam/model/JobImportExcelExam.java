package com.app.toeic.exam.model;

import com.app.toeic.util.Constant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_import_excel_exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobImportExcelExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String examName;
    String audio;
    String excel;
    Integer topicId;

    @Builder.Default
    String status = Constant.STATUS_IN_PROGRESS;

    @CreationTimestamp
    LocalDateTime startTime;

    @UpdateTimestamp
    LocalDateTime endTime;
}
