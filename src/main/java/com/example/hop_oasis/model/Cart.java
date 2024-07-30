package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;


}
