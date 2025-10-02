package com.app.toeic.email.repo;

import com.app.toeic.email.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfigRepo extends JpaRepository<EmailConfig, Integer> {
    Optional<EmailConfig> findByUsername(String username);

    @Modifying
    @Query("update EmailConfig e set e.status = ?2 where e.username != ?1")
    void updateAllByUsernameNot(String username, String status);
}
