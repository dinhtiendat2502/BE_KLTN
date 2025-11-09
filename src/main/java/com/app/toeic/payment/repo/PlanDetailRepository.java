package com.app.toeic.payment.repo;

import com.app.toeic.payment.model.PlanDetail;
import com.app.toeic.payment.response.PlanDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Integer> {


    @Query("""
                        SELECT pd
                        FROM PlanDetail pd
                        JOIN FETCH pd.plans p
                        WHERE p.planId = ?1
            """)
    List<PlanDetailInfo> findAllByPlans(Integer planId);


    @Query("""
            SELECT pd
            FROM PlanDetail pd
            """)
    List<PlanDetailInfo> findAllPlanDetail();
}
