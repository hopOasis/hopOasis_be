package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "beer")
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "beer_name")
    private String beerName;
    @Column(name = "volume_large")
    private double volumeLarge;
    @Column(name = "volume_small")
    private double volumeSmall;
    @Column(name = "price_large")
    private double priceLarge;
    @Column(name = "price_small")
    private double priceSmall;
    @Column(name = "description")
    private String description;
    @Column(name = "beer_color")
    private String bearColor;

    @OneToMany(mappedBy = "beer",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Image> image;
}
