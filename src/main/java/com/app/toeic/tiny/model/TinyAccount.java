package com.app.toeic.tiny.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tiny_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TinyAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tinyAccountId;

    @Column(unique = true)
    private String username;
    private String password;

    @Builder.Default
    private boolean status = true;
}
