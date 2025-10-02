package com.app.toeic.revai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "revai_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevAIAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private String status = "INACTIVE";
}
