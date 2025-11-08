package com.app.toeic.komunicate.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kommunicate_bot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KommunicateBot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String appId;
    String apiKey;

    @Column(columnDefinition = "TEXT")
    String script;

    @Builder.Default
    boolean status = false;
}
