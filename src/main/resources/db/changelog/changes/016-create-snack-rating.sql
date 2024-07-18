CREATE TABLE IF NOT EXISTS snack_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating DOUBLE NOT NULL,
    snack_id INT NOT NULL,
    CONSTRAINT fk_snack_rating FOREIGN KEY (snack_id) REFERENCES snacks(id) ON DELETE CASCADE
);