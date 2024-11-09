package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "products_bundle")
public class ProductBundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "productBundle", cascade = CascadeType.ALL)
    private List<ProductBundleOptions> productBundleOptions;


    @OneToMany(mappedBy = "productBundle", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProductBundleImage> productImage;

    @ManyToMany
    @JoinTable(
            name = "product_bundle_special_offer_product",
            joinColumns = @JoinColumn(name = "product_bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
    )
    private List<SpecialOfferProduct> specialOfferProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductBundle that = (ProductBundle) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
