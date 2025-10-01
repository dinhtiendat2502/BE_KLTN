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
    private String projectId;
    private String fileJson;
    private String status;
}
