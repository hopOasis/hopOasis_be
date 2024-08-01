package com.example.hop_oasis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private Long itemId;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
}
