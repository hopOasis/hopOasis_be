CREATE TABLE IF NOT EXISTS snacks_images
(
  id
  INT
  PRIMARY
  KEY
  AUTO_INCREMENT,
  image
  LONGBLOB
  NOT
  NULL,
  name
  VARCHAR
(
  500
) NOT NULL,
  snack_id INT,
  CONSTRAINT fk_snacks FOREIGN KEY
(
  snack_id
) REFERENCES snacks
(
  id
) ON DELETE CASCADE

  );