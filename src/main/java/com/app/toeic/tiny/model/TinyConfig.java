package com.app.toeic.tiny.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tiny_config")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Builder
public class TinyConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tinyConfigId;

    @Column(unique = true)
    private String apiKey = "8b794dn1wu635cyq3thspfbaxifw3gqoghn9gbc442kgjf8j";

    @Builder.Default
    private boolean status = false;
}

