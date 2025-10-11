package com.app.toeic.tiny.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tiny_config")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TinyConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tinyConfigId;

    @Column(unique = true)
    private String apiKey;

    @Builder.Default
    private boolean status = false;
}
