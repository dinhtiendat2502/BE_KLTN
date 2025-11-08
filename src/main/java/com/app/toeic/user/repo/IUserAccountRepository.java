package com.app.toeic.user.repo;

import com.app.toeic.user.model.Role;
import com.app.toeic.user.model.UserAccount;
import com.app.toeic.user.response.UserAccountRepsonse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);

    @Query(value = """
            select
                ua.user_id,
                ua.email,
                ua.full_name,
                ua.phone,
                ua.provider,
                ua.avatar,
                ua.address,
                ua.status
            from user_account ua
            where ua.email = ?1
            """, nativeQuery = true)
    Optional<UserAccountRepsonse> getByEmail(String email);

    List<UserAccount> findAllByRolesNotContains(Role role);

    @Query(value = """
            select
                    ua.user_id,
                    ua.email,
                    ua.full_name,
                    ua.phone,
                    ua.provider,
                    ua.avatar,
                    ua.address,
                    ua.status
            from user_account ua
            inner join user_role ur on ur.user_id = ua.user_id
            inner join role r on r.role_id=ur.role_id and r.role_name = 'USER'
            """, nativeQuery = true)
    Page<UserAccountRepsonse> findAllUser(Pageable pageable);

    @Query(value = """
            select
                    ua.user_id,
            ua.email,
            ua.full_name,
            ua.phone,
            ua.provider,
            ua.avatar,
            ua.address,
            ua.status
            from user_account ua
            inner join user_role ur on ur.user_id = ua.user_id
            inner join role r on r.role_id=ur.role_id and r.role_name = 'USER'
            WHERE ua.status =?1
            """, nativeQuery = true)
    Page<UserAccountRepsonse> findAllUserByStatus(String status, Pageable pageable);
}
