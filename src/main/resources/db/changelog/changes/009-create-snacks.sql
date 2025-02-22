CREATE TABLE IF NOT EXISTS snacks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    snack_name VARCHAR(100) NOT NULL,
    weight_large INTEGER NOT NULL,
    weight_small INTEGER NOT NULL,
    price_large DECIMAL(7, 2) NOT NULL,
    price_small DECIMAL(7, 2) NOT NULL,
    description VARCHAR(1500) NOT NULL

);