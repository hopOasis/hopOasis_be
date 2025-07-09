package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FedExRateRequestDto {
    private String senderPostalCode;
    private String recipientPostalCode;
    private double weight;
    private String serviceType;
}
