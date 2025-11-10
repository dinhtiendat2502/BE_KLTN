package com.app.toeic.course.repo;

import com.app.toeic.course.model.CategoryCourse;
import com.app.toeic.course.response.CategoryCourseInfo;
import com.app.toeic.course.response.CategoryCourseInfoV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryCourseRepo extends JpaRepository<CategoryCourse, Integer> {
    List<CategoryCourse> findAllByStatusOrderByPositionDesc(String status);
    Optional<CategoryCourse> findFirstByOrderByPositionDesc();

    @Query("""
                    SELECT c
                    FROM CategoryCourse c
                    JOIN FETCH c.courses cc
                    WHERE c.status = 'ACTIVE' AND cc.status = :status
                    ORDER BY c.position DESC
            """)
    List<CategoryCourseInfoV2> findAllOrderByPosition(String status);

    @Query("""
                    SELECT c
                    FROM CategoryCourse c
                    WHERE c.status = 'ACTIVE'
                    ORDER BY c.position
            """)
    List<CategoryCourseInfo> findAllOrderByPosition();
}
