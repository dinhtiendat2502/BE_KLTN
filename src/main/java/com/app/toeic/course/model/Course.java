package com.app.toeic.course.model;

import com.app.toeic.course.payload.CourseRequest;
import com.app.toeic.util.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "course", indexes = {
        @Index(name = "name", columnList = "name"),
        @Index(name = "status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer courseId;
    String name;
    @Builder.Default
    int numberMember = 0;
    @Builder.Default
    BigDecimal price = BigDecimal.ZERO;
    int discount;
    @Builder.Default
    BigDecimal priceSale = BigDecimal.ZERO;
    @Column(columnDefinition = "TEXT")
    String thumbnail; // link image
    @Column(columnDefinition = "TEXT")
    String courseOverview; // mô tả khóa học
    @Column(columnDefinition = "TEXT")
    String courseObjective; // hoc duoc gi sau khi hoc xong
    @Column(columnDefinition = "TEXT")
    String courseContent; // noi dung khoa hoc
    @Column(columnDefinition = "TEXT")
    String courseSyllabus; // Cac chu de cua khoa hoc
    @Column(columnDefinition = "TEXT")
    String courseReview; // danh gia cua hoc vien

    @Builder.Default
    String status = Constant.STATUS_ACTIVE;

    @ManyToOne
    @JoinColumn(name = "category_course_id")
    CategoryCourse categoryCourse;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonBackReference
    Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonBackReference
    Set<CourseRegister> courseRegisters = new HashSet<>();

    public void withUpdate(CourseRequest request) {
        this.name = StringUtils.defaultIfBlank(request.name(), this.name);
        this.price = Optional.ofNullable(request.price()).orElse(this.price);
        this.discount = request.discount();
        this.priceSale = Optional.ofNullable(request.salePrice()).orElse(this.priceSale);
        this.courseOverview = request.courseOverview();
        this.courseObjective = request.courseObjective();
        this.courseContent = request.courseContent();
        this.courseSyllabus = request.courseSyllabus();
        this.courseReview = request.courseReview();
    }

    public static Course from(CourseRequest request) {
        return Course.builder()
                     .name(request.name())
                     .price(request.price())
                     .discount(request.discount())
                     .priceSale(request.salePrice())
                     .courseOverview(request.courseOverview())
                     .courseObjective(request.courseObjective())
                     .courseContent(request.courseContent())
                     .courseSyllabus(request.courseSyllabus())
                     .courseReview(request.courseReview())
                     .build();
    }
}
