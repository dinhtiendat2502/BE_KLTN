package com.app.toeic.payment.response;

import java.math.BigDecimal;

/**
 * Projection for {@link com.app.toeic.payment.model.Plans}
 */
public interface PlansInfo {
    Integer getPlanId();

    String getPlanName();

    BigDecimal getPlanPrice();
    String getDescription();
}
