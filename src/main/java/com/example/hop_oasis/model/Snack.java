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
@Table(name = "snacks")
public class Snack {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "snack_name")
  private String snackName;
  @Column(name = "weight_large")
  private double weightLarge;
  @Column(name = "weight_small")
  private double weightSmall;
  @Column(name = "price_large")
  private double priceLarge;
  @Column(name = "price_small")
  private double priceSmall;
  @Column(name = "description")
  private String description;

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
