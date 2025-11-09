package com.app.toeic.message.model;

import com.app.toeic.user.model.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long conversationId;

    String title;

    @JsonIgnore
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "creator_id")
    UserAccount creator;

    @ManyToMany(mappedBy = "conversations")
    @Builder.Default
    @JsonBackReference
    Set<UserAccount> participants = new HashSet<>();

    @OneToMany(mappedBy = "conversation")
    @Builder.Default
    @JsonBackReference
    Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "conversation")
    @Builder.Default
    @JsonBackReference
    Set<Participant> conversationParticipants = new HashSet<>();
}
