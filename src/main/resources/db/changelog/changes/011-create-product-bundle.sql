CREATE TABLE IF NOT EXISTS products_bundle
(
  id
  INT
  AUTO_INCREMENT
  PRIMARY
  KEY,
  product_name
  VARCHAR
(
  100
) NOT NULL,
  price DECIMAL
(
  7,
  2
) NOT NULL,
  description VARCHAR
(
  500
) NOT NULL

  );