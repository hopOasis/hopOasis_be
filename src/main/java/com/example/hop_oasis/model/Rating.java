package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private double rating;

    public Rating(Long itemId, ItemType itemType, double rating){
        this.itemId = itemId;
        this.itemType = itemType;
        this.rating = rating;
    }

}
