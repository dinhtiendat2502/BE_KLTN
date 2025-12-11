package com.app.toeic.firebase.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "firebase_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirebaseConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String tokenKey;
    String bucketName;

    @Column(unique = true)
    String projectId;

    @Column(columnDefinition = "text")
    String fileJson;

    @Builder.Default
    boolean status = false;
}
