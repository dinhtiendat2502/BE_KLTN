package com.app.toeic.repository;

import com.app.toeic.enums.ERole;
import com.app.toeic.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(ERole roleName);
}
