CREATE TABLE IF NOT EXISTS cider_options (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      cider_id BIGINT NOT NULL,
      volume DECIMAL(7,2) NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(7,2) NOT NULL,
      FOREIGN KEY (cider_id) REFERENCES cider(id) ON DELETE CASCADE
)