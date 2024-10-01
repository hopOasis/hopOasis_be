CREATE TABLE IF NOT EXISTS snack_options (
      id INT AUTO_INCREMENT PRIMARY KEY,
      snack_id INT NOT NULL,
      weight DECIMAL(7,2) NOT NULL,
      quantity INT NOT NULL,
      price DECIMAL(7,2) NOT NULL,
      FOREIGN KEY (snack_id) REFERENCES snacks(id) ON DELETE CASCADE
)