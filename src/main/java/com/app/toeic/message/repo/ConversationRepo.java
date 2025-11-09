package com.app.toeic.message.repo;

import com.app.toeic.message.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Long> {
}
