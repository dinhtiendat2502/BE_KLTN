package com.app.toeic.repository;

import com.app.toeic.model.UserAccountAvatarLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserAccountAvatarLogRepository extends JpaRepository<UserAccountAvatarLog, Integer> {
}
