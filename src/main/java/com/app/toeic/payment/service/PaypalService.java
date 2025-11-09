package com.app.toeic.payment.service;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.payment.model.Currency;
import com.app.toeic.payment.response.PaypalOrder;
import com.app.toeic.user.service.UserService;
import com.app.toeic.util.Constant;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaypalService {
    PayPalHttpClient payPalHttpClient;
    SystemConfigService systemConfigService;
    public String createPayment(BigDecimal fee) {
        var returnUrl = systemConfigService.getConfigValue(Constant.PAYPAL_RETURN_URL);
        var orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        fee = fee.divide(BigDecimal.valueOf(23000), 2, RoundingMode.HALF_UP);
        var amountBreakdown = new AmountWithBreakdown().currencyCode(Currency.USD.name()).value(fee.toString());
        var purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown);
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        var applicationContext = new ApplicationContext()
                .returnUrl(MessageFormat.format("{0}?s={1}", returnUrl, "success"))
                .cancelUrl(MessageFormat.format("{0}?s={1}", returnUrl, "cancel"));
        orderRequest.applicationContext(applicationContext);
        var ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);
        try {
            var orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
            var order = orderHttpResponse.result();

            return order.links().stream()
                        .filter(link -> "approve".equals(link.rel()))
                        .findFirst()
                        .orElseThrow(NoSuchElementException::new)
                        .href();
        } catch (IOException e) {
            log.severe(e.getMessage());
            throw new AppException("400", "Error when create payment order");
        }
    }

    public String completePayment(String token) {
        var ordersCaptureRequest = new OrdersCaptureRequest(token);
        try {
            var httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            if (httpResponse.result().status() != null) {
                return httpResponse.result().status();
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
        throw new AppException("400", "Error when create payment order");
    }
}
