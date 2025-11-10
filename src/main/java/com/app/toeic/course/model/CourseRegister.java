package com.app.toeic.course.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_register", indexes = {
        @Index(name = "name", columnList = "name"),
        @Index(name = "phone", columnList = "phone")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer courseRegisterId;
    String name;
    String phone;
    String email;

    @Builder.Default
    Boolean status = false;     // true: đã xác nhận, false: chưa xác nhận ( cần gọi điện hoặc gửi email xác nhận)

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @CreationTimestamp
    @Builder.Default
    LocalDateTime createdDate = LocalDateTime.now();

    @UpdateTimestamp
    @Builder.Default
    LocalDateTime updatedDate = LocalDateTime.now();
}
