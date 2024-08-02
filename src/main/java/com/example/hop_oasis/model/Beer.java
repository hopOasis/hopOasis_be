package com.example.hop_oasis.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
  @Column(name = "beer_color")
  private String beerColor;

  @OneToMany(mappedBy = "beer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Image> image;

  @ManyToMany
  @JoinTable(
      name = "beer_special_offer_product",
      joinColumns = @JoinColumn(name = "beer_id"),
      inverseJoinColumns = @JoinColumn(name = "special_offer_product_id")
  )
  private List<SpecialOfferProduct> specialOfferProduct;
}
