package com.app.toeic.firebase.repo;

import com.app.toeic.firebase.model.FirebaseUploadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FirebaseUploadHistoryRepo extends JpaRepository<FirebaseUploadHistory, Integer> {

    @Query("SELECT f FROM FirebaseUploadHistory f WHERE f.fileType like %?1%")
    Page<FirebaseUploadHistory> findAllByFileType(String fileType, Pageable pageable);

    @Query("SELECT COALESCE(SUM(CAST(f.fileSize AS java.math.BigInteger)), 0) FROM FirebaseUploadHistory f WHERE f.fileType like %?1%")
    BigInteger sumByFileSize(String fileType);

    @Query("SELECT COALESCE(SUM(CAST(f.fileSize AS java.math.BigInteger)), 0) FROM FirebaseUploadHistory f")
    BigInteger sumByFileSize();
}
