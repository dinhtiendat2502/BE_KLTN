package com.app.toeic.leftmenu.repo;

import com.app.toeic.leftmenu.model.LeftMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeftMenuRepo extends JpaRepository<LeftMenu, Long>{
}
