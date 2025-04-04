package com.example.hop_oasis.service.data;

import com.example.hop_oasis.enums.EmailMessage;
import com.example.hop_oasis.enums.OrderStatus;
import com.example.hop_oasis.model.EmailNotificationLog;
import com.example.hop_oasis.model.Order;
import com.example.hop_oasis.repository.EmailNotificationLogRepository;
import com.example.hop_oasis.utils.EmailPattern;
import com.example.hop_oasis.utils.OrderStatusTranslate;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${sendgrid.api.key}")
    private final String sendGridApiKey;

    private final TemplateEngine templateEngine;
    private final EmailNotificationLogRepository emailLogRepository;
    private final EmailSettingsService settingsService;

    public void sendOrderStatusUpdateEmail(Order order) {
        if (!settingsService.isEmailNotificationsEnabled()) {
            log.info("Email notifications are disabled. Skipping email for order {}", order.getId());
            return;
        }
        String toEmail = order.getUser().getEmail();
        String userName = order.getUser().getFirstName();
        String status = order.getOrderStatus().name();

        Context context = new Context();
        context.setVariable("firstName", userName);
        context.setVariable("orderNumber", order.getOrderNumber());
        context.setVariable("orderStatus", OrderStatusTranslate.translateStatus(OrderStatus.valueOf(status)));
        context.setVariable("orderItems", order.getOrderItems() != null ? order.getOrderItems() : List.of());
        context.setVariable("totalSum", order.getTotalPrice());
        context.setVariable("currency", "ГРН");

        String emailContent = templateEngine.process("email-template", context);

        boolean sentSuccessfully = sendEmail(toEmail, "Order Status Update", emailContent);

        if (!sentSuccessfully) {
            log.error("Failed to send email to {}", toEmail);
        }

        emailLogRepository.save(new EmailNotificationLog(
                order,
                order.getUser(),
                toEmail,
                order.getOrderStatus(),
                LocalDateTime.now(),
                sentSuccessfully ? EmailMessage.SENT : EmailMessage.SENDING_ERROR
        ));
    }

    public boolean sendEmail(String toEmail, String subject, String body) {
        try {
            if (sendGridApiKey.isBlank()) {
                log.error("SendGrid API Key is missing! Email will not be sent.");
                return false;
            }

            Email from = new Email(EmailPattern.EMAIL_SENDER);
            Email to = new Email(toEmail);
            Content content = new Content("text/html", body);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint(EmailPattern.ENDPOINT);
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.info("Email sent to {}! Status code: {}", toEmail, response.getStatusCode());

            return response.getStatusCode() == 202;

        } catch (IOException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
            return false;
        }
    }

    public String buildOrderConfirmationEmail(Order order) {
        Context context = new Context();
        context.setVariable("firstName", order.getUser().getFirstName());
        context.setVariable("lastName", order.getUser().getLastName());
        context.setVariable("orderNumber", order.getOrderNumber());

        return templateEngine.process("order-confirmation", context);
    }
}