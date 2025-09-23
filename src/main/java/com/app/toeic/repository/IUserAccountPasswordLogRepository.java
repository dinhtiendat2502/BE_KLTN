package com.app.toeic.repository;

import com.app.toeic.model.UserAccountPasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserAccountPasswordLogRepository extends JpaRepository<UserAccountPasswordLog, Integer> {
}
