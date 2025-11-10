package com.app.toeic.course.model;

import com.app.toeic.util.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "category_course", indexes = {
        @Index(name = "name", columnList = "name"),
        @Index(name = "status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer categoryCourseId;
    String name;
    @Builder.Default
    int position = 0;

    @Builder.Default
    String status = Constant.STATUS_ACTIVE;

    @OneToMany(mappedBy = "categoryCourse", cascade = CascadeType.ALL)
    @JsonBackReference
    @Builder.Default
    Set<Course> courses = new HashSet<>();
}
