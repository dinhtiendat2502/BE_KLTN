package com.app.toeic.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userAccountLogId;

    @Lob
    @Column(name = "old_data", length = 1000)
    private String oldData;

    @Lob
    @Column(name = "new_data", length = 1000)
    private String newData;

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
