package com.app.toeic.payment.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.payment.model.StripeRequest;
import com.app.toeic.payment.service.StripeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment/stripe")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripeController {
    StripeService stripeService;

    @PostMapping("create")
    @SneakyThrows
    public Object createPayment(@RequestBody StripeBody body) {
        var request = StripeRequest
                .builder()
                .amount(body.amount())
                .name(body.name())
                .stripeEmail(body.email())
                .build();
        return ResponseVO
                .builder().success(true)
                .data(stripeService.createCheckout(request))
                .build();
    }

    public record StripeBody(String name, Long amount, String email) {
    }
}
