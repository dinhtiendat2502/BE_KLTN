package com.app.toeic.leftmenu.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long menuGroupId;
    String displayName;
    String icon;
    int priority;
    String path;
    String roles;

    @Builder.Default
    String type = "MEMBER";

    @Builder.Default
    boolean haveChild = false;

    @OneToMany(mappedBy = "menuGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    Set<LeftMenu> leftMenus = new HashSet<>();
}
