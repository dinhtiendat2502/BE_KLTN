package com.app.toeic.leftmenu.repo;

import com.app.toeic.leftmenu.model.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepo extends JpaRepository<MenuGroup, Long>{
}
