package com.app.toeic.chatai.repo;

import com.app.toeic.chatai.model.ChatAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatAiRepository extends JpaRepository<ChatAI, Integer> {
    List<ChatAI> findAllByStatusAndType(boolean status, String type);
}
