package com.app.toeic.firebase.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "firebase_upload_history", indexes = {
        @Index(name = "idx_file_type", columnList = "fileType"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirebaseUploadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String fileName;
    String fileUrl;
    String fileType;
    long fileSize;
    @CreationTimestamp
    LocalDateTime uploadDate;
}
