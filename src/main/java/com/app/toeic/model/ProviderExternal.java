package com.app.toeic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provider_external")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderExternal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer providerId;
    private String providerName;

    @org.hibernate.annotations.Index(name = "provider_code_index")
    private String providerCode;
    private String providerLink;
    private String username;
    private String password;
    @Column(columnDefinition = "TEXT")
    private String cookie;
    @Column(columnDefinition = "TEXT")
    private String config;
    private String status;
}
