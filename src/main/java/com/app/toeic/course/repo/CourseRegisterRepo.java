package com.app.toeic.course.repo;

import com.app.toeic.course.model.CourseRegister;
import com.app.toeic.course.response.CourseRegisterInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRegisterRepo extends JpaRepository<CourseRegister, Integer> {
    @Query("""
                    SELECT c
                    FROM CourseRegister  c
                    WHERE c.status = false
                    ORDER BY c.createdDate
            """)
    Page<CourseRegisterInfo> findAllOrderByCreatedDate(Pageable pageable);
}
