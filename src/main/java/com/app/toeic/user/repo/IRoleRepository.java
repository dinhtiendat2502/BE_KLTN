package com.app.toeic.user.repo;

import com.app.toeic.user.enums.ERole;
import com.app.toeic.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(ERole roleName);
}
