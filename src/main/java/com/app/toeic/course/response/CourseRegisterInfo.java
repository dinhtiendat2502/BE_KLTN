package com.app.toeic.course.response;

import java.time.LocalDateTime;

/**
 * Projection for {@link com.app.toeic.course.model.CourseRegister}
 */
public interface CourseRegisterInfo {
    Integer getCourseRegisterId();

    String getName();

    String getPhone();

    String getEmail();

    Boolean getStatus();

    LocalDateTime getCreatedDate();
}
