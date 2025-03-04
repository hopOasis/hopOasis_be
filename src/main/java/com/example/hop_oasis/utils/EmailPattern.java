package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Order;

public class EmailPattern {
    public static String buildOrderConfirmationEmail(Order order, String firstName, String lastName) {
        return "Шановний " + firstName + " " + lastName + "\n\n"
                + "Ваше замовлення було оброблено і вже в дорозі." +
                " Ми повідомимо, коли товар буде доставлено." + "\n\n"
                + "Номер замовлення: " + order.getOrderNumber() + "\n\n"
                + "З повагою,\nкоманда Hoppy Oasis";
    }
}
