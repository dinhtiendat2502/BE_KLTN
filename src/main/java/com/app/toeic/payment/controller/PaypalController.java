package com.app.toeic.payment.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.payment.service.PaypalService;
import com.app.toeic.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("payment/paypal")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaypalController {
    PaypalService paypalService;
    UserService userService;

    @PostMapping("create")
    @SneakyThrows
    public Object createPayment(@RequestBody BigDecimal sum) {
        System.out.println(sum);
        return ResponseVO
                .builder().success(true)
                .data(paypalService.createPayment(sum))
                .build();
    }

    @PostMapping("confirm")
    public Object confirmPayment(@RequestParam("token") String token, HttpServletRequest request) {
        var rs = ResponseVO.builder().success(false).build();
        var success = paypalService.completePayment(token);
        if ("COMPLETED".equals(success)) {
            userService.updateUserType(request);
            rs.setSuccess(true);
        }
        return rs;
    }
}
