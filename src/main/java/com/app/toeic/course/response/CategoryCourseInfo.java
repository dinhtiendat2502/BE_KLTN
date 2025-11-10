package com.app.toeic.course.response;

import com.app.toeic.course.model.CategoryCourse;
import java.util.Set;

/**
 * Projection for {@link CategoryCourse}
 */
public interface CategoryCourseInfo {
    Integer getCategoryCourseId();

    String getName();

    int getPosition();

    String getStatus();

}
