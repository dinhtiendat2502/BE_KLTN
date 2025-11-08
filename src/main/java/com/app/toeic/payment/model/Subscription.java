package com.app.toeic.payment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription", indexes = {
        @Index(name = "idx_subscription_end_date", columnList = "end_date"),
        @Index(name = "idx_subscription_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long subscriptionId;

    @Builder.Default
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate;
    LocalDateTime unsubscriptionDate;

    BigDecimal amount;
    String paymentMethod;
    String status;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    Plans plan;
}
