package com.app.toeic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_avatar_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountAvatarLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAccountAvatarLogId;

    @Lob
    @Column(name = "old_avatar_data", length = 1000)
    private String oldAvatarData;

    @Lob
    @Column(name = "new_avatar_data", length = 1000)
    private String newAvatarData;

    private String country;
    private String action;
    private String lastUpdatedBy;
    private String lastIpAddress;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserAccount userAccount;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
