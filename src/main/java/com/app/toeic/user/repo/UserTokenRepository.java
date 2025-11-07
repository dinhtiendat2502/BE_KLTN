package com.app.toeic.user.repo;

import com.app.toeic.user.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    Optional<UserToken> findByEmail(String email);
}
