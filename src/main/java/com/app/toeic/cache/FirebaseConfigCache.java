package com.app.toeic.cache;

import com.app.toeic.firebase.model.FirebaseConfig;
import com.app.toeic.firebase.repo.FirebaseRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Log
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FirebaseConfigCache {
    FirebaseRepository firebaseRepository;

    CacheLoader<Boolean, FirebaseConfig> cacheLoader = new CacheLoader<>() {
        @NotNull
        @Override
        public FirebaseConfig load(@NotNull Boolean key) {
            return firebaseRepository
                    .findAllByStatus(key)
                    .stream()
                    .findFirst()
                    .orElse(FirebaseConfig.builder().build());
        }
    };

    LoadingCache<Boolean, FirebaseConfig> systemCache = CacheBuilder.newBuilder()
            .recordStats()
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(CacheLoader.asyncReloading(cacheLoader, Executors.newSingleThreadExecutor()));

    public FirebaseConfig getConfigValue(Boolean key) {
        try {
            return systemCache.get(key);
        } catch (Exception e) {
            log.log(Level.WARNING, "FirebaseConfigCache >> getConfigValue >> Exception: ", e);
        }
        return FirebaseConfig.builder().build();
    }

    public void invalidateCache() {
        systemCache.invalidateAll();
    }
}
