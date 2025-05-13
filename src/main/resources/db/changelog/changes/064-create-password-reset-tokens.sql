CREATE TABLE IF NOT EXISTS reset_password_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date DATETIME NOT NULL,
    CONSTRAINT fk_user_reset_token
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);
