CREATE TABLE IF NOT EXISTS products_images(
    id INT  PRIMARY KEY AUTO_INCREMENT,
    image LONGBLOB NOT NULL,
    name VARCHAR(500) NOT NULL,
    product_image_id INT,
    CONSTRAINT fk_products_bundle FOREIGN KEY (product_image_id) REFERENCES products_bundle(id) ON DELETE CASCADE

);