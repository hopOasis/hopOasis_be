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
@Table(name = "snacks")
public class Snack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "snack_name")
    private String snackName;
    @Column(name = "weight_large")
    private double weightLarge;
    @Column(name = "weight_small")
    private double weightSmall;
    @Column(name = "price_large")
    private double priceLarge;
    @Column(name = "price_small")
    private double priceSmall;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "snack",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<SnackImage> snackImage;
    @ManyToOne
    @JoinColumn(name = "snack_id")
    private SpecialOfferProduct specialOfferProduct;
}
