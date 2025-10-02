package com.app.toeic.firebase.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "firebase_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirebaseConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tokenKey;
    private String bucketName;

    @Column(unique = true)
    private String projectId;

    @Column(columnDefinition = "text")
    private String fileJson;

    @Column(columnDefinition = "varchar(255) default 'ACTIVE'")
    @Builder.Default
    private String status = "INACTIVE";
}
