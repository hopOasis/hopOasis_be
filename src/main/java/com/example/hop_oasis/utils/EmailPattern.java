package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Order;

public class EmailPattern {
    public static String ENDPOINT = "mail/send";
    public static String EMAIL_SENDER = "service@hoppyoasis.my";
    public static String EMAIL_TITLE = "Дякуємо за замовлення!";

    public static String buildOrderConfirmationEmail(Order order, String firstName, String lastName) {
        return "Шановний " + firstName + " " + lastName + "\n\n"
                + "Ваше замовлення було оброблено і вже в дорозі." +
                " Ми повідомимо, коли товар буде доставлено." + "\n\n"
                + "Номер замовлення: " + order.getOrderNumber() + "\n\n"
                + "З повагою,\nкоманда Hoppy Oasis";
    }

    public static String buildOrderNotPaidEmail(Order order, String firstName, String lastName) {
        return "Шановний " + firstName + " " + lastName + "\n\n"
                + "Ваше замовлення не було сплачене." +
                " Спробуйте ще раз." + "\n\n"
                + "Номер замовлення: " + order.getOrderNumber() + "\n\n"
                + "З повагою,\nкоманда Hoppy Oasis";
    }
}
