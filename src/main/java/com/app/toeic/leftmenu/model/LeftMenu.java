package com.app.toeic.leftmenu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "left_menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeftMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long leftMenuId;
    String path;
    String displayName;
    String roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    @JsonBackReference
    MenuGroup menuGroup;
}
