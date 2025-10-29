package com.app.toeic.komunicate.model;

import com.app.toeic.util.Constant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kommunicate_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KommunicateAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private String status = Constant.STATUS_INACTIVE;
}
