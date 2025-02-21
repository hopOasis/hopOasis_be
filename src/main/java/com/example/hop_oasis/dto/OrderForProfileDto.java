package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.DeliveryMethod;
import com.example.hop_oasis.enums.DeliveryStatus;
import com.example.hop_oasis.enums.DeliveryType;
import com.example.hop_oasis.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderForProfileDto {
    private Long id;
    private String orderNumber;
    private PaymentType paymentType;
    private String customerPhoneNumber;
    private DeliveryType deliveryType;
    private DeliveryMethod deliveryMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private DeliveryStatus deliveryStatus;
    private BigDecimal totalPrice;
    private List<OrderItemDto> items;
}
