package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductBundleOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_bundle_id")
    private ProductBundle productBundle;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;
}
