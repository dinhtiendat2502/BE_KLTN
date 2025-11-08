package com.app.toeic.email.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_config", indexes = {
        @Index(name = "status_index", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String host;
    private int port;
    @Column(unique = true)
    private String username;
    private String password;

    @Builder.Default
    private boolean status = false;
}
