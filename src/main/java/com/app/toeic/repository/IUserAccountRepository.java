package com.app.toeic.repository;

import com.app.toeic.model.UserAccount;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserAccountRepository extends JpaRepository<UserAccount, String> {
    Boolean existsByEmail(String email);
    Optional<UserAccount> findByEmail(String email);
}
