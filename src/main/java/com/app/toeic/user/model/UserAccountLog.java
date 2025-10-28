package com.app.toeic.user.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAccountLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userAccountLogId;

    @Lob
    String oldData;

    @Lob
    String newData;

    String action;
    String description;

    @Builder.Default
    String lastUpdatedBy = "SYSTEM";
    String lastIpAddress;

    @ManyToOne
    @JoinColumn(name = "userId")
    UserAccount userAccount;

    @CreationTimestamp
    LocalDateTime createdAt;
}
