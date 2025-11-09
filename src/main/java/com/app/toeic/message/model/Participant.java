package com.app.toeic.message.model;

import com.app.toeic.user.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant {
    @EmbeddedId
    ConversationParticipantId conversationParticipantId;

    @ManyToOne
    @MapsId("conversationId")
    @JoinColumn(name = "conversation_id")
    Conversation conversation;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserAccount user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ParticipantType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedAt;

    public enum ParticipantType {
        SINGLE, GROUP
    }
}
