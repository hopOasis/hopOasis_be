CREATE TABLE IF NOT EXISTS cider_images
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
  cider_id INT,
  CONSTRAINT fk_cider FOREIGN KEY
(
  cider_id
) REFERENCES cider
(
  id
) ON DELETE CASCADE

  );