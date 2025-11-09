package com.app.toeic.payment.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.payment.model.PlanDetail;
import com.app.toeic.payment.model.Plans;
import com.app.toeic.payment.repo.PlanDetailRepository;
import com.app.toeic.payment.repo.PlanRepository;
import com.app.toeic.payment.response.PlanDetailResponse;
import com.app.toeic.payment.response.PlanResponse;
import com.app.toeic.payment.response.PlansInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("plan")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanController {
    PlanRepository planRepository;
    PlanDetailRepository planDetailRepository;

    @GetMapping("get")
    @Transactional
    public Object getPlanFormMember() {
        var listPlan = planRepository.findAll();
        var listPlanDetail = planDetailRepository.findAll();
        var rs = new PlanResponse();
        listPlan.forEach(e -> rs.getListPlan()
                                .add(new PlanResponse.PlanRs(e.getPlanId(), e.getPlanName(), e.getPlanPrice(), e.getDescription())));
        listPlanDetail.forEach(e -> {
            var planDetailResponse = PlanDetailResponse.builder()
                                                       .planDetailId(e.getPlanDetailId())
                                                       .planDetailName(e.getPlanDetailName())
                                                       .build();
            var listStatus = listPlan.stream().map(e1 -> e1.getPlanMapping().contains(e)).toList();
            planDetailResponse.setListStatus(listStatus);
            rs.getListDetail().add(planDetailResponse);
        });
        return ResponseVO
                .builder()
                .data(rs)
                .success(true)
                .message("GET_PLAN_SUCCESS")
                .build();
    }

    @GetMapping("list")
    public Object getAllPlan() {
        return ResponseVO
                .builder()
                .data(planRepository.findAllPlan())
                .success(true)
                .message("GET_ALL_PLAN_SUCCESS")
                .build();
    }

    @GetMapping("list-detail")
    public Object getAllPlanDetail() {
        return ResponseVO
                .builder()
                .data(planDetailRepository.findAllPlanDetail())
                .success(true)
                .message("GET_ALL_PLAN_DETAIL_SUCCESS")
                .build();
    }

    @GetMapping("list-detail-by-id")
    public Object getListDetailByPlanId(@RequestParam("planId") Integer planId) {
        return ResponseVO
                .builder()
                .data(planDetailRepository.findAllByPlans(planId))
                .success(true)
                .message("GET_LIST_DETAIL_BY_PLAN_ID_SUCCESS")
                .build();
    }

    @PostMapping("update-plan")
    public Object updatePlan(@RequestBody PlanDTO planDTO) {
        var plan = planRepository.findById(planDTO.planId == null ? -1 : planDTO.planId);
        plan.ifPresentOrElse(e -> {
            e.setPlanName(planDTO.planName());
            e.setPlanPrice(planDTO.planPrice());
            e.setDescription(planDTO.description());
            e.setUpdatedAt(LocalDateTime.now());
            planRepository.save(e);
        }, () -> planRepository.save(Plans.builder()
                                          .planName(planDTO.planName())
                                          .planPrice(planDTO.planPrice())
                                          .description(planDTO.description())
                                          .build()));
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_PLAN_SUCCESS")
                .build();
    }

    @PatchMapping("update-plan-detail-status")
    @Transactional
    public Object updatePlanDetailStatus(@RequestBody UpdatePlanDetailStatusDTO payload) {
        var rs = ResponseVO.builder().success(false).message("UPDATE_FAIL").build();
        var plan = planRepository.findById(payload.planId() == null ? -1 : payload.planId());
        plan.ifPresent(e -> {
            var planDetail = planDetailRepository.findById(payload.planDetailId() == null ? -1 : payload.planDetailId());
            planDetail.ifPresent(e1 -> {
                if (payload.active()) {
                    e.getPlanMapping().add(e1);
                } else {
                    e.getPlanMapping().remove(e1);
                }
                planRepository.save(e);
                rs.setSuccess(true);
                rs.setMessage("UPDATE_PLAN_DETAIL_STATUS_SUCCESS");
            });
        });
        return rs;
    }

    @DeleteMapping("delete-plan/{planId}")
    @Transactional
    public Object deletePlan(@PathVariable Integer planId) {
        var rs = ResponseVO.builder().success(false).message("DELETE_FAIL").build();
        var plan = planRepository.findById(planId);
        plan.ifPresent(e -> {
            e.getPlanMapping().forEach(e1 -> e1.getPlans().remove(e));
            e.getPlanMapping().clear();
            planRepository.delete(e);
            rs.setSuccess(true);
            rs.setMessage("DELETE_PLAN_SUCCESS");
        });
        return rs;
    }

    @DeleteMapping("delete-plan-detail/{planDetailId}")
    @Transactional
    public Object deletePlanDetail(@PathVariable Integer planDetailId) {
        var rs = ResponseVO.builder().success(false).message("DELETE_FAIL").build();
        var planDetail = planDetailRepository.findById(planDetailId);
        planDetail.ifPresent(e -> {
            e.getPlans().forEach(e1 -> e1.getPlanMapping().remove(e));
            e.getPlans().clear();
            planDetailRepository.delete(e);
            rs.setSuccess(true);
            rs.setMessage("DELETE_PLAN_DETAIL_SUCCESS");
        });
        return rs;
    }

    @PostMapping("update-plan-detail")
    public Object updatePlanDetail(@RequestBody PlanDetailDTO planDetailDTO) {
        var planDetail = planDetailRepository.findById(planDetailDTO.planDetailId == null ? -1 : planDetailDTO.planDetailId);
        planDetail.ifPresentOrElse(
                e -> {
                    e.setPlanDetailName(planDetailDTO.planDetailName());
                    planDetailRepository.save(e);
                },
                () -> planDetailRepository
                        .save(PlanDetail.builder()
                                        .planDetailName(planDetailDTO.planDetailName())
                                        .build())
        );
        return ResponseVO
                .builder()
                .success(true)
                .message("UPDATE_PLAN_DETAIL_SUCCESS")
                .build();
    }


    public record PlanDTO(Integer planId, String planName, BigDecimal planPrice, String description) {
    }

    public record PlanDetailDTO(Integer planDetailId, String planDetailName) {
    }

    public record UpdatePlanDetailStatusDTO(Integer planId, Integer planDetailId, boolean active) {
    }
}
