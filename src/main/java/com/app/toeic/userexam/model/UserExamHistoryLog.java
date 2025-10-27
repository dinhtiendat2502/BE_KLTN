package com.app.toeic.userexam.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "user_exam_history_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserExamHistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userExamHistoryLogId;
}
