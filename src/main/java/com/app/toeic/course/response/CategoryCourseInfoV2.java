package com.app.toeic.course.response;

import java.util.Set;

public interface CategoryCourseInfoV2 extends CategoryCourseInfo {
    Set<CourseInfo> getCourses();
}
