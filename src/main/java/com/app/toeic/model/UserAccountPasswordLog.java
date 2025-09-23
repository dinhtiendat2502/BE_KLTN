package com.app.toeic.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_password_log")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserAccountPasswordLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(nullable = false)
    private String oldPassword;

    @Column(nullable = false)
    private String newPassword;

    private String action;
    private String country;
    private String lastUpdatedBy;
    private String lastIpAddress;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
