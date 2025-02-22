CREATE TABLE IF NOT EXISTS product_bundle_options (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      product_bundle_id BIGINT NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(7,2) NOT NULL,
      FOREIGN KEY (product_bundle_id) REFERENCES products_bundle(id) ON DELETE CASCADE
)