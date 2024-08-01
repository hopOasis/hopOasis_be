package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name ="products_bundle")
public class ProductBundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="product_name")
    private String name;
    @Column(name="price")
    private double price;
    @Column(name ="description")
    private String description;

    @OneToMany(mappedBy = "productBundle", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<ProductBundleImage> productImage;

    @ManyToMany
    @JoinTable(
            name = "product_bundle_special_offer_product",
            joinColumns = @JoinColumn(name = "product_bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
    )
    private List<SpecialOfferProduct> specialOfferProduct;
}
