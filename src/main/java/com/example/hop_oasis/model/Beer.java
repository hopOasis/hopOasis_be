package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;


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
    @Column(name = "description")
    private String description;
    @Column(name = "beer_color")
    private String beerColor;
    @OneToMany(mappedBy = "beer", cascade = CascadeType.ALL)
    private List<BeerOptions> beerOptions;

    @OneToMany(mappedBy = "beer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Image> image;

    @ManyToMany
    @JoinTable(
            name = "beer_special_offer_product",
            joinColumns = @JoinColumn(name = "beer_id"),
            inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
    )
    private List<SpecialOfferProduct> specialOfferProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Beer beer = (Beer) o;
        return Objects.equals(id, beer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
