package com.app.toeic.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.model.ProviderExternal;
import com.app.toeic.repository.ProviderExternalRepository;
import com.app.toeic.service.ProviderExternalService;
import com.app.toeic.util.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProviderExternalServiceImpl implements ProviderExternalService {
    ProviderExternalRepository providerExternalRepository;

    @Override
    public ProviderExternal getProviderExternalByCode(String code) {
        return providerExternalRepository
                .getProviderExternalByProviderCode(code)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Provider not found"));
    }
}
