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
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "snack", cascade = CascadeType.ALL)
    private List<SnackOptions> snackOptions;

    @OneToMany(mappedBy = "snack", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<SnackImage> snackImage;
    @ManyToMany
    @JoinTable(
            name = "snack_special_offer_product",
            joinColumns = @JoinColumn(name = "snack_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
    )
    private List<SpecialOfferProduct> specialOfferProduct;
}
