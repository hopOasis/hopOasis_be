CREATE TABLE cart_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    measure_value DOUBLE NOT NULL,
    item_type VARCHAR(20) NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart(id)
);