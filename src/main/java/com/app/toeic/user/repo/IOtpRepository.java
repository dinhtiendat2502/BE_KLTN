package com.app.toeic.user.repo;

import com.app.toeic.user.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOtpRepository extends JpaRepository<Otp, Long> {
}
