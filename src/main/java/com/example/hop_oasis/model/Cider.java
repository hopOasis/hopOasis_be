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
@Table(name = "cider")
public class Cider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "cider_name")
    private String ciderName;
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

    @OneToMany(mappedBy = "cider", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CiderImage> image;

    @ManyToMany
    @JoinTable(
            name = "cider_special_offer_product",
            joinColumns = @JoinColumn(name = "cider_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
    )
    private List<SpecialOfferProduct> specialOfferProduct;
}
