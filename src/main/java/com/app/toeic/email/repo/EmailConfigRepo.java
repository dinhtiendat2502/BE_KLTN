package com.app.toeic.email.repo;

import com.app.toeic.email.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailConfigRepo extends JpaRepository<EmailConfig, Integer> {
    Optional<EmailConfig> findByUsername(String username);
    List<EmailConfig> findAllByUsernameNot(String username);
}
