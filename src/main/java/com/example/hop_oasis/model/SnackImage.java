package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "snacks_images")
public class SnackImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Lob
    @Column(name = "image",length = 100000)
    private byte[] image;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "snack_id")
    private Snack snack;
}