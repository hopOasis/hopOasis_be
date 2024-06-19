package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "cider_images")
public class CiderImage {
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
    @JoinColumn(name = "cider_id")
    private Cider cider;
}
