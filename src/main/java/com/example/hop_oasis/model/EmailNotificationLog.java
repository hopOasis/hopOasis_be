package com.example.hop_oasis.model;

import com.example.hop_oasis.enums.EmailMessage;
import com.example.hop_oasis.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "email_notification_log")
public class EmailNotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
    @Column(name = "message")
    @Enumerated(EnumType.STRING)
    private EmailMessage message;

    public EmailNotificationLog(Order order, User user, String email, OrderStatus orderStatus, LocalDateTime sentAt, EmailMessage message) {
        this.order = order;
        this.user = user;
        this.email = email;
        this.orderStatus = orderStatus;
        this.sentAt = sentAt;
        this.message = message;
    }
}
