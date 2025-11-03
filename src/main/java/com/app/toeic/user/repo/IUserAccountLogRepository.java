package com.app.toeic.user.repo;

import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.model.UserAccountLog;
import com.app.toeic.user.response.UserActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IUserAccountLogRepository extends JpaRepository<UserAccountLog, Integer> {
    Page<UserActivityResponse> findAllByUserAccount(UserAccount userAccount, Pageable pageable);
    Page<UserActivityResponse> findAllByUserAccountAndAction(UserAccount userAccount, String action, Pageable pageable);
    Page<UserActivityResponse.UserActivity2Response> findAllByActionAndCreatedAtBetween(String action, LocalDateTime createdAt, LocalDateTime createdAt2, Pageable pageable     );
    Page<UserActivityResponse.UserActivity2Response> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
