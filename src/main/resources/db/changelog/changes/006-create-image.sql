CREATE TABLE IF NOT EXISTS images
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
  beer_id INT,
  CONSTRAINT fk_beer FOREIGN KEY
(
  beer_id
) REFERENCES beer
(
  id
) ON DELETE CASCADE

  );