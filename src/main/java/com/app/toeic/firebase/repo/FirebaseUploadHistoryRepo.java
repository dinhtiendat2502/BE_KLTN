package com.app.toeic.firebase.repo;

import com.app.toeic.firebase.model.FirebaseUploadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Repository
public interface FirebaseUploadHistoryRepo extends JpaRepository<FirebaseUploadHistory, Integer> {

    @Query("SELECT f FROM FirebaseUploadHistory f WHERE f.fileType like %?1%")
    Page<FirebaseUploadHistory> findAllByFileTypeAndUploadDateBetween(String fileType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<FirebaseUploadHistory> findAllByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(CAST(f.fileSize AS java.math.BigInteger)), 0)
            FROM FirebaseUploadHistory f
            WHERE f.fileType like %?1% AND f.uploadDate BETWEEN ?2 AND ?3
            """)
    BigInteger sumByFileSizeAndUploadDateBetween(String fileType, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
                    SELECT COALESCE(SUM(CAST(f.fileSize AS java.math.BigInteger)), 0)
                    FROM FirebaseUploadHistory f
                    WHERE f.uploadDate BETWEEN ?1 AND ?2
            """)
    BigInteger sumByFileSizeAndUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
