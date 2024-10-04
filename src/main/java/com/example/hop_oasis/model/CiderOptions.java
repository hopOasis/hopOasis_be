package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "cider_options")
public class CiderOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cider_id")
    private Cider cider;
    @Column(name = "volume")
    private Double volume;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;

}
