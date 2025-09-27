package com.app.toeic.external.repository;

import com.app.toeic.external.model.ProviderExternal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProviderExternalRepository extends JpaRepository<ProviderExternal, Integer> {
    Optional<ProviderExternal> getProviderExternalByProviderCode(String code);
}
