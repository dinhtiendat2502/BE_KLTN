package com.app.toeic.config;

import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {
    @Bean
    public PayPalHttpClient getPaypalClient(SystemConfigService systemConfigService) {
        return new PayPalHttpClient(new PayPalEnvironment.Sandbox(
                systemConfigService.getConfigValue(Constant.PAYPAL_CLIENT_ID),
                systemConfigService.getConfigValue(Constant.PAYPAL_CLIENT_SECRET)
        ));
    }
}
