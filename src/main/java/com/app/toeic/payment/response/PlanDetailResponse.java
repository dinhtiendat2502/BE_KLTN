package com.app.toeic.payment.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDetailResponse {
    Integer planDetailId;
    String planDetailName;
    @Builder.Default
    List<Boolean> listStatus = new ArrayList<>();
}
