package com.app.toeic.komunicate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kommunicate_bot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KommunicateBot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String appId;
    private String apiKey;

    @Builder.Default
    private String status = "INACTIVE";
}
