package com.app.toeic.email.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "email_config", indexes = {
        @Index(name = "status_index", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String host;
    int port;
    @Column(unique = true)
    String username;
    String password;

    @Builder.Default
    boolean status = false;
}
