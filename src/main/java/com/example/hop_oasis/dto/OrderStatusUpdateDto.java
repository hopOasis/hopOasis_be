package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.OrderStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderStatusUpdateDto {
    private Long orderId;
    private OrderStatus newStatus;
    private String cancellationReason;
}
