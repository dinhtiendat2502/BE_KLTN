package com.app.toeic.transcript.repo;

import com.app.toeic.transcript.model.TranscriptHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptRepo extends JpaRepository<TranscriptHistory, Long> {
}
