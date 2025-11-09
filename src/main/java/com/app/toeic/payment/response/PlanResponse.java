package com.app.toeic.payment.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanResponse {
    @Builder.Default
    List<PlanRs> listPlan = new ArrayList<>();
    @Builder.Default
    List<PlanDetailResponse> listDetail = new ArrayList<>();

    public record PlanRs(Integer planId, String planName, BigDecimal planPrice, String description) {
    }
}
