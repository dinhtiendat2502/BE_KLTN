package com.app.toeic.revai.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "revai_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevAIConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String accessToken;

    @Builder.Default
    boolean status = false;
}
