CREATE TABLE IF NOT EXISTS beer_options (
      id INT AUTO_INCREMENT PRIMARY KEY,
      beer_id INT,
      volume DECIMAL(7,2),
      quantity INT,
      price DECIMAL(7,2),
      FOREIGN KEY (beer_id) REFERENCES beer(id) ON DELETE CASCADE
)