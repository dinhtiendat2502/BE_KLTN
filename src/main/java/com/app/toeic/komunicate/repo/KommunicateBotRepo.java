package com.app.toeic.komunicate.repo;

import com.app.toeic.komunicate.model.KommunicateBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KommunicateBotRepo extends JpaRepository<KommunicateBot, Integer> {
    Optional<KommunicateBot> findByAppId(String appId);

    List<KommunicateBot> findAllByAppIdNot(String appId);
    List<KommunicateBot> findAllByStatus(boolean status);
}
