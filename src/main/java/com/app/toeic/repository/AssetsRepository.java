package com.app.toeic.repository;

import com.app.toeic.model.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AssetsRepository extends JpaRepository<Assets, Integer> {
    Optional<Assets> findByPath(String path);
}
