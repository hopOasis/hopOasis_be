package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "special_offer_product")
public class SpecialOfferProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "specialOfferProduct",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Beer> beers;

    @OneToMany(mappedBy = "specialOfferProduct",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Cider> ciders;

    @OneToMany(mappedBy = "specialOfferProduct",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Snack> snacks;

    @OneToMany(mappedBy = "specialOfferProduct",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<ProductBundle> productBundles;

}
