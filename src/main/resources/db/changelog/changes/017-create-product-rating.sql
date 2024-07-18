CREATE TABLE IF NOT EXISTS product_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating DOUBLE NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_product_rating FOREIGN KEY (product_id) REFERENCES products_bundle(id) ON DELETE CASCADE
);