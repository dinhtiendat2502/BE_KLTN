package com.app.toeic.userexam.model;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_exam_history_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExamHistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userExamHistoryLogId;


}
