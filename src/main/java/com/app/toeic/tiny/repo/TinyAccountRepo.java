package com.app.toeic.tiny.repo;

import com.app.toeic.tiny.model.TinyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TinyAccountRepo extends JpaRepository<TinyAccount, Long>{
    Optional<TinyAccount> findByUsername(String username);
}
