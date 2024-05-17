CREATE TABLE IF NOT EXISTS beer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    beer_name VARCHAR(100) NOT NULL,
    volume_large DECIMAL(7, 2) NOT NULL,
    volume_small DECIMAL(7, 2) NOT NULL,
    price_large DECIMAL(7, 2) NOT NULL,
    price_small DECIMAL(7, 2) NOT NULL,
    description VARCHAR(500) NOT NULL,
    bear_color VARCHAR(100) NOT NULL,
    image BLOB NOT NULL
);






