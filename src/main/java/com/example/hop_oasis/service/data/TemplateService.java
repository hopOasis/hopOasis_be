package com.example.hop_oasis.service.data;

import com.example.hop_oasis.enums.OrderStatus;
import com.example.hop_oasis.model.Order;
import com.example.hop_oasis.utils.OrderStatusTranslate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateEngine templateEngine;

    public String buildInvoiceHtml(Order order) {
        Context context = new Context();
        context.setVariable("firstName", order.getUser().getFirstName());
        context.setVariable("lastName", order.getUser().getLastName());
        context.setVariable("orderNumber", order.getOrderNumber());
        context.setVariable("invoiceDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        context.setVariable("deliveryAddress", order.getDeliveryAddress());
        context.setVariable("orderItems", order.getOrderItems());
        context.setVariable("totalSum", order.getTotalPrice());
        return templateEngine.process("invoice-template", context);
    }

    public String buildOrderConfirmationEmailHtml(Order order) {
        Context context = new Context();
        context.setVariable("firstName", order.getUser().getFirstName());
        context.setVariable("lastName", order.getUser().getLastName());
        context.setVariable("orderNumber", order.getOrderNumber());
        return templateEngine.process("order-confirmation", context);
    }

    public IContext buildOrderStatusContext(Order order) {
        String userName = order.getUser().getFirstName();
        String status = order.getOrderStatus().name();
        Context context = new Context();
        context.setVariable("firstName", userName);
        context.setVariable("orderNumber", order.getOrderNumber());
        context.setVariable("orderStatus", OrderStatusTranslate.translateStatus(OrderStatus.valueOf(status)));
        context.setVariable("orderItems", order.getOrderItems() != null ? order.getOrderItems() : List.of());
        context.setVariable("totalSum", order.getTotalPrice());
        context.setVariable("currency", "ГРН");
        return context;
    }
}

