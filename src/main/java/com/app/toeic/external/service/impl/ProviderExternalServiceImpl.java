package com.app.toeic.external.service.impl;

import com.app.toeic.exception.AppException;
import com.app.toeic.external.model.ProviderExternal;
import com.app.toeic.external.repository.ProviderExternalRepository;
import com.app.toeic.external.service.ProviderExternalService;
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
