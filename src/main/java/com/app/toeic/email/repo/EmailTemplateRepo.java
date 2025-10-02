package com.app.toeic.email.repo;

import com.app.toeic.email.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepo extends JpaRepository<EmailTemplate, Integer> {
    Optional<EmailTemplate> findByTemplateCode(String templateCode);
}
