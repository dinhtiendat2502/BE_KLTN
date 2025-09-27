package com.app.toeic.external.service;


import com.app.toeic.external.model.ProviderExternal;

public interface ProviderExternalService {
    ProviderExternal getProviderExternalByCode(String code);
}
