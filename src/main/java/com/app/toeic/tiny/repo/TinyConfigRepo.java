package com.app.toeic.tiny.repo;

import com.app.toeic.tiny.model.TinyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinyConfigRepo extends JpaRepository<TinyConfig, Long> {
    List<TinyConfig> findAllByTinyConfigIdNot(Long tinyConfigId);
}
