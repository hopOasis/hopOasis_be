package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "beer_options")
public class BeerOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;
    @Column(name = "volume")
    private double volume;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;

}
