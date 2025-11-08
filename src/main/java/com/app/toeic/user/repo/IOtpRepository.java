package com.app.toeic.user.repo;

import com.app.toeic.user.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndAction(String email, String action);
}
