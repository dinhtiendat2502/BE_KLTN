package com.app.toeic.service;


import com.app.toeic.model.ProviderExternal;
import org.springframework.stereotype.Service;

public interface ProviderExternalService {
    ProviderExternal getProviderExternalByCode(String code);
}
