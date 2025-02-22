CREATE TABLE IF NOT EXISTS beer_options (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      beer_id BIGINT NOT NULL,
      volume DECIMAL(7,2) NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(7,2) NOT NULL,
      FOREIGN KEY (beer_id) REFERENCES beer(id) ON DELETE CASCADE
)