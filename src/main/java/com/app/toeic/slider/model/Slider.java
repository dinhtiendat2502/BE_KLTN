package com.app.toeic.slider.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "slider")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    @Column(unique = true)
    private Integer position;

}
