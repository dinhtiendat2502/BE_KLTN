package com.app.toeic.firebase.repo;

import com.app.toeic.firebase.model.FirebaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FirebaseRepository extends JpaRepository<FirebaseConfig, Integer>{
    List<FirebaseConfig> findAllByIdNot(Integer id);

    Optional<FirebaseConfig> findByProjectId(String projectId);

    List<FirebaseConfig> findAllByStatus(boolean status);
}
