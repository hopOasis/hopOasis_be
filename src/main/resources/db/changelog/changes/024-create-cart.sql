CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    item_type VARCHAR(20) NOT NULL
);
