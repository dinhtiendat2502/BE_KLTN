package com.app.toeic.repository;

import com.app.toeic.model.UserAccount;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Boolean existsByEmail(String email);
    Optional<UserAccount> findByEmail(String email);
}
