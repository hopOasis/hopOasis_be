package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "products_images")
public class ProductBundleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "product_image_id")
    private ProductBundle productBundle;

}
