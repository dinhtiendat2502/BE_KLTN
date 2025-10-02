package com.app.toeic.komunicate.repo;

import com.app.toeic.komunicate.model.KommunicateAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KommunicateAccountRepo extends JpaRepository<KommunicateAccount, Integer> {
    Optional<KommunicateAccount> findByEmail(String email);
}
