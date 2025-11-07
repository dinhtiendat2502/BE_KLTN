package com.app.toeic.user.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_token", indexes = {
        @Index(name = "email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer tokenId;

    @Column(nullable = false, columnDefinition = "TEXT")
    String token;
    String email;

    LocalDateTime expiredDate;
    LocalDateTime createdDate;
}
