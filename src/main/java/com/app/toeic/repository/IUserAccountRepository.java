package com.app.toeic.repository;

import com.app.toeic.model.Role;
import com.app.toeic.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);

    List<UserAccount> findAllByRolesNotContains(Role role);
}
