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
    Integer userAccountLogId;

    @Lob
    @Column(name = "old_data", length = 1000)
    String oldData;

    @Lob
    @Column(name = "new_data", length = 1000)
    String newData;

    String country;
    String action;
    String lastUpdatedBy;
    String lastIpAddress;

    @ManyToOne
    @JoinColumn(name = "userId")
    UserAccount userAccount;

    @CreationTimestamp
    LocalDateTime createdAt;
}
