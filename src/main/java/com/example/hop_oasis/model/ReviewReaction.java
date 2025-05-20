package com.example.hop_oasis.model;

import com.example.hop_oasis.enums.Reaction;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "review_reactions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "review_id"}))
public class ReviewReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    private Reaction reaction;
}
