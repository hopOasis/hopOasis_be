package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.DeliveryMethod;
import com.example.hop_oasis.enums.DeliveryStatus;
import com.example.hop_oasis.enums.DeliveryType;
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
    private DeliveryType deliveryType;
    private DeliveryMethod deliveryMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private DeliveryStatus deliveryStatus;
    private BigDecimal totalPrice;
    private List<OrderItemDto> items;
}
