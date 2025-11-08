package com.app.toeic.payment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonIgnore
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "plan_mapping",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "plan_detail_id")
    )
    @Builder.Default
    Set<PlanDetail> planMapping = new HashSet<>();

    @OneToMany(mappedBy = "plan")
    @Builder.Default
    @JsonBackReference
    Set<Subscription> subscriptions = new HashSet<>();
}
