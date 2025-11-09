package com.app.toeic.payment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripeRequest {
    @Builder.Default
    String description = "Thanh toán phí thành viên Toeicute";
    Long amount;
    String name;

    @Builder.Default
    Long quantity = 1L;

    @Builder.Default
    Currency currency = Currency.VND;
    String stripeEmail;
    String stripeToken;
}
