package com.app.toeic.user.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_action", columnList = "action")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long otpId;

    String otpCode; // OTP code
    String email;
    String action;

    @CreationTimestamp
    LocalDateTime createdAt;
}
