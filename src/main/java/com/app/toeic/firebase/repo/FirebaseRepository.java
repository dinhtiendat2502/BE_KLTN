package com.app.toeic.firebase.repo;

import com.app.toeic.firebase.model.FirebaseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseRepository extends JpaRepository<FirebaseConfig, Integer>{
}
