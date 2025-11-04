package com.app.toeic.external.service;

import com.app.toeic.external.model.SystemConfig;
import com.app.toeic.external.repo.SystemConfigRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemConfigService {

    SystemConfigRepository systemConfigRepository;

    CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
        @NotNull
        @Override
        public String load(@NotNull String key) {
            return systemConfigRepository.findByConfigKey(key).orElse(new SystemConfig()).getValue();
        }
    };

    LoadingCache<String, String> systemCache = CacheBuilder.newBuilder()
                                                           .recordStats()
                                                           .refreshAfterWrite(1, TimeUnit.DAYS)
                                                           .build(CacheLoader.asyncReloading(cacheLoader, Executors.newSingleThreadExecutor()));

    public String getConfigValue(String key) {
        try {
            return systemCache.get(key);
        } catch (ExecutionException e) {
            log.log(Level.WARNING, "SystemConfigService >> getConfigValue >> ExecutionException: ", e);
            return StringUtils.EMPTY;
        }
    }
}
