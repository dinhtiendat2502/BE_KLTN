package com.app.toeic.course.payload;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record CourseRequest(
        Integer courseId
        , String name
        , BigDecimal price
        , int discount
        , BigDecimal salePrice
        , String thumbnail
        , String courseOverview
        , String courseObjective
        , String courseContent
        , String courseSyllabus
        , String courseReview
        , Integer categoryCourseId
        , MultipartFile file
) {
}
