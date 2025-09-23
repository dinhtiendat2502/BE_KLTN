package com.app.toeic.repository;

import com.app.toeic.model.UserAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserAccountLogRepository extends JpaRepository<UserAccountLog, Integer> {
}
