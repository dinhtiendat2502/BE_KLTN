package com.app.toeic.user.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userAccountLogId;

    @Lob
    @Column(name = "old_data", length = 1000)
    private String oldData;

    @Lob
    @Column(name = "new_data", length = 1000)
    private String newData;

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
