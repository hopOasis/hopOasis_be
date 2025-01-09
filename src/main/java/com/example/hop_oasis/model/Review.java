package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "item_id")
    private Long itemId;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private ItemType itemType;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "content")
    private String content;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }


}

