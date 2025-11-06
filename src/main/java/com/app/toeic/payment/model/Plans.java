package com.app.toeic.payment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer planId;

    String planName;
    @Column(columnDefinition = "TEXT")
    String description;
    BigDecimal planPrice;
    byte planDuration;

    @Builder.Default
    boolean isActive = true;

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plan")
    @Builder.Default
    Set<Subscription> subscriptions = new HashSet<>();
}
