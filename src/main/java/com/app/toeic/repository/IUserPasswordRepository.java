package com.app.toeic.repository;

import com.app.toeic.model.UserPasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserPasswordRepository extends JpaRepository<UserPasswordLog, Integer> {
}
