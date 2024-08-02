package com.example.hop_oasis.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  @Column(name = "name_offer")
  private String name;

  @Column(name = "active")
  private boolean active = false;


  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "beer_special_offer_product",
      joinColumns = @JoinColumn(name = "special_offer_product_id"),
      inverseJoinColumns = @JoinColumn(name = "beer_id"))
  private List<Beer> beers;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "cider_special_offer_product",
      joinColumns = @JoinColumn(name = "special_offer_product_id"),
      inverseJoinColumns = @JoinColumn(name = "cider_id"))
  private List<Cider> ciders;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "snack_special_offer_product",
      joinColumns = @JoinColumn(name = "special_offer_product_id"),
      inverseJoinColumns = @JoinColumn(name = "snack_id"))
  private List<Snack> snacks;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "product_bundle_special_offer_product",
      joinColumns = @JoinColumn(name = "special_offer_product_id"),
      inverseJoinColumns = @JoinColumn(name = "product_bundle_id"))
  private List<ProductBundle> productBundles;


}
