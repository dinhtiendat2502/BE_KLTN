package com.app.toeic.transcript.repo;

import com.app.toeic.transcript.model.TranscriptHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TranscriptRepo extends JpaRepository<TranscriptHistory, Long> {
    Page<TranscriptHistory> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<TranscriptHistory> findAllByCreatedAtBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status, Pageable pageable);
}
