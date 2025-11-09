package com.app.toeic.payment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaypalOrder {
    private String status;
    private String payId;
    private String redirectUrl;

    public PaypalOrder(String status) {
        this(status, null, null);
    }
    public PaypalOrder(String status, String payId) {
       this(status, payId, null);
    }
}
