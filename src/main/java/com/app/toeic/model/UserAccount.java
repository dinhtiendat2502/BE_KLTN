package com.app.toeic.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private String email;
    private String phone;
    private String address;
    private String avatar;

    // Enum ('ACTIVE', 'INACTIVE', 'BLOCKED')
    private String status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
