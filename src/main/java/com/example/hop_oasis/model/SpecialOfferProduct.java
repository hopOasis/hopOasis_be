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


    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "beer_special_offer_product",
            joinColumns = @JoinColumn(name = "special_offer_product_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id"))
    private List<Beer> beers;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "cider_special_offer_product",
            joinColumns = @JoinColumn(name = "special_offer_product_id"),
            inverseJoinColumns = @JoinColumn(name = "cider_id"))
    private List<Cider> ciders;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "snack_special_offer_product",
            joinColumns = @JoinColumn(name = "special_offer_product_id"),
            inverseJoinColumns = @JoinColumn(name = "snack_id"))
    private List<Snack> snacks;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "product_bundle_special_offer_product",
            joinColumns = @JoinColumn(name = "special_offer_product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_bundle_id"))
    private List<ProductBundle> productBundles;


}
