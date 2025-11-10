package com.app.toeic.course.repo;

import com.app.toeic.course.model.Course;
import com.app.toeic.course.response.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Integer> {
    @Query("""
            SELECT c
            FROM Course c
            JOIN FETCH c.categoryCourse cc
            WHERE c.status = :status AND cc.status = 'ACTIVE'
            """)
    List<CourseInfo> findAllByStatus(String status);

    @Query("""
            SELECT c
            FROM Course c
            JOIN FETCH c.categoryCourse cc
            WHERE c.courseId = :courseId AND c.status = 'ACTIVE' AND cc.status = 'ACTIVE'
            """)
    Optional<CourseInfo> findDetailByCourseId(Integer courseId);
}
