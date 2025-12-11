package com.app.toeic.payment.service;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.payment.model.StripeRequest;
import com.app.toeic.util.Constant;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripeService {
    SystemConfigService systemConfigService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = systemConfigService.getConfigValue(Constant.STRIPE_API_KEY);
    }

    public Object createCheckout(StripeRequest request) throws StripeException {
        var returnUrl = systemConfigService.getConfigValue(Constant.STRIPE_RETURN_URL);
        var productData = SessionCreateParams
                .LineItem.PriceData.ProductData
                .builder()
                .setName(request.getName())
                .setDescription(request.getDescription())
                .build();
        var priceData = SessionCreateParams
                .LineItem.PriceData
                .builder()
                .setCurrency(request.getCurrency().name())
                .setUnitAmount(request.getAmount())
                .setProductData(productData)
                .build();
        var lineItem = SessionCreateParams
                .LineItem
                .builder()
                .setQuantity(request.getQuantity())
                .setPriceData(priceData)
                .build();
        var sessionParam = SessionCreateParams
                .builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(request.getStripeEmail())
                .setSuccessUrl(MessageFormat.format("{0}?s=success", returnUrl))
                .setCancelUrl(MessageFormat.format("{0}?s=cancel", returnUrl))
                .addLineItem(lineItem)
                .build();
        var session = Session.create(sessionParam);
        return session.getUrl();
    }
}
