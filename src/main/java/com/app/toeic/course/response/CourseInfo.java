package com.app.toeic.course.response;

import java.math.BigDecimal;

public interface CourseInfo {
    Integer getCourseId();

    String getName();

    int getNumberMember();

    BigDecimal getPrice();

    int getDiscount();

    BigDecimal getPriceSale();

    String getThumbnail();

    String getCourseOverview();

    String getCourseObjective();

    String getCourseContent();

    String getCourseSyllabus();

    String getCourseReview();

    String getStatus();
}
