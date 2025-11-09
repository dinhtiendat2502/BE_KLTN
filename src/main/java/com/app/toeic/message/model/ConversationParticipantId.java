package com.app.toeic.message.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ConversationParticipantId implements Serializable {
    Long conversationId;
    Integer userId;
}
