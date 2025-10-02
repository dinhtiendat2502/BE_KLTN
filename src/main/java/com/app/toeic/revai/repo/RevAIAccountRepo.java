package com.app.toeic.revai.repo;

import com.app.toeic.revai.model.RevAIAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevAIAccountRepo extends JpaRepository<RevAIAccount, Integer>{
    Optional<RevAIAccount> findByEmail(String email);
}
