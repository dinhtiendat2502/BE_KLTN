package com.app.toeic.message.model;

import com.app.toeic.user.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    UserAccount sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    String body;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;
}
