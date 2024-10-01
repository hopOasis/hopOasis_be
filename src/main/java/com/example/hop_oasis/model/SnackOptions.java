package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "snack_options")
public class SnackOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "snack_id")
    private Snack snack;
    @Column(name = "weight")
    private double weight;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;
}
