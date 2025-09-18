package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "team_recommendations")
public class TeamRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "author_name", nullable = false)
    private String authorName;
    @Column(name = "team_role", nullable = false)
    private String teamRole;
    @Column(name = "text_recommendation", nullable = false)
    private String textRecommendation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
