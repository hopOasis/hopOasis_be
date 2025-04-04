package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.DeliveryMethod;
import com.example.hop_oasis.enums.OrderStatus;
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
    private DeliveryMethod deliveryMethod;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private List<OrderItemDto> items;
}
