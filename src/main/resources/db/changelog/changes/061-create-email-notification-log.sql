CREATE TABLE email_notification_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    sent_at DATETIME NOT NULL,
    error_message VARCHAR(255) NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
