package com.example.hop_oasis.utils;

import com.example.hop_oasis.enums.OrderStatus;

import java.util.Map;

public class OrderStatusTranslate {
    private static final Map<OrderStatus, String> ORDER_STATUS_TRANSLATION = Map.of(
            OrderStatus.PROCESSING, "«В обробці»",
            OrderStatus.ACCEPTED, "«Прийнято»",
            OrderStatus.IN_PROGRESS, "«В роботі»",
            OrderStatus.DELIVERED, "«Доставлено»",
            OrderStatus.COMPLETED, "«Завершено»",
            OrderStatus.CANCELLED, "«Відмінено»"

    );

    public static String translateStatus(OrderStatus status) {
        return ORDER_STATUS_TRANSLATION.getOrDefault(status, status.name());
    }
}
