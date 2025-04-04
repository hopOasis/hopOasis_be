package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.DeliveryMethod;
import com.example.hop_oasis.enums.OrderStatus;
import com.example.hop_oasis.enums.PaymentStatus;
import com.example.hop_oasis.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String orderNumber;
    private PaymentType paymentType;
    private String customerPhoneNumber;
    private String customerEmail;
    private String firstName;
    private String lastName;
    private DeliveryMethod deliveryMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private String cancellationReason;
    private BigDecimal totalPrice;
    private List<OrderItemDto> items;
    private PaymentStatus paymentStatus;
}
