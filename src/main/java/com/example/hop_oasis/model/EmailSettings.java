package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "email_settings")
public class EmailSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email_notifications_enabled", nullable = false)
    private boolean emailNotificationsEnabled;
}
