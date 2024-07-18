package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BeerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "rating", nullable = false)
    private double ratingValue;
    @ManyToOne
    @JoinColumn(name = "beer_id", nullable = false)
    private Beer beer;
}