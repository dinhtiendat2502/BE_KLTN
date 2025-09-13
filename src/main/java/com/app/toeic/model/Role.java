package com.app.toeic.model;


import com.app.toeic.enums.ERole;
import com.app.toeic.enums.EUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer roleId;

    @Enumerated(EnumType.STRING)
    ERole roleName;

    public Role(ERole roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName.toString();
    }
}
