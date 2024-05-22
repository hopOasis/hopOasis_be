package com.example.hop_oasis.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "payment_data")
public class PaymentData {
    @Id
    @Column(name = "user_details_id")
    private Long id;

    @Column(name = "card_number")
    private int cardNumber;

    @Column(name = "cvv")
    private int cvv;

    @Column(name = "expiry_date")
    private String expiryDate;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    private UserProfile userProfile;



}
