package com.app.toeic.revai.repo;

import com.app.toeic.revai.model.RevAIConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevAIConfigRepo extends JpaRepository<RevAIConfig, Integer>{
    List<RevAIConfig> findAllByAccessTokenNot(String accessToken);
}
