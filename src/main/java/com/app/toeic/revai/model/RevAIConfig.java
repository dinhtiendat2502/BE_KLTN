package com.app.toeic.revai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "revai_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevAIConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accessToken;

    @Builder.Default
    private boolean status = false;
}
