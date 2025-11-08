package com.app.toeic.leftmenu.repo;

import com.app.toeic.leftmenu.model.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuGroupRepo extends JpaRepository<MenuGroup, Long> {

    @Query("""
            SELECT mg
            FROM MenuGroup mg
            LEFT JOIN FETCH mg.leftMenus lm
            ORDER BY mg.priority, lm.leftMenuId
            """)
    List<MenuGroup> findAllMenu();
}
