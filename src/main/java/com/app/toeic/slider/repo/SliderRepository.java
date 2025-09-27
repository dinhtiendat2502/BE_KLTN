package com.app.toeic.slider.repo;

import com.app.toeic.slider.model.Slider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {
    Optional<Slider> findByPosition(Integer position);

    @Query("SELECT s FROM Slider s ORDER BY s.position DESC LIMIT 1")
    Optional<Slider> findLastByPosition();

    @Query("SELECT s FROM Slider s WHERE s.position > ?1 ORDER BY s.position")
    List<Slider> findAllByPositionGreaterThanOrderByPosition(Integer position);
}
