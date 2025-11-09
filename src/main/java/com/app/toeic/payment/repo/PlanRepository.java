package com.app.toeic.payment.repo;

import com.app.toeic.payment.model.Plans;
import com.app.toeic.payment.response.PlansInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plans, Integer> {
    @Query("""
            SELECT p
            FROM Plans p
            """)
    List<PlansInfo> findAllPlan();
}
