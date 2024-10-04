CREATE TABLE IF NOT EXISTS beer_options (
      id INT AUTO_INCREMENT PRIMARY KEY,
      beer_id INT NOT NULL,
      volume DECIMAL(7,2) NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(7,2) NOT NULL,
      FOREIGN KEY (beer_id) REFERENCES beer(id) ON DELETE CASCADE
)