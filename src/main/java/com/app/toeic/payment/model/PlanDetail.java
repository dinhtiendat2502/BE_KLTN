package com.app.toeic.payment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plan_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer planDetailId;
    String planDetailName;

    @ManyToMany(mappedBy = "planMapping")
    @Builder.Default
    Set<Plans> plans = new HashSet<>();

}
