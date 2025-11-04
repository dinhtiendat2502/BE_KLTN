package com.app.toeic.external.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_config", indexes = {
        @Index(name = "idx_config_key", columnList = "configKey", unique = true)
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer systemConfigId;

    String configKey;

    @Column(columnDefinition = "TEXT")
    String value;
    String description;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
