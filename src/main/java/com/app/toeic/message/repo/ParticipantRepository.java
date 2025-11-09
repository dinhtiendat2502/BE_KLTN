package com.app.toeic.message.repo;

import com.app.toeic.message.model.ConversationParticipantId;
import com.app.toeic.message.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ConversationParticipantId> {
}
