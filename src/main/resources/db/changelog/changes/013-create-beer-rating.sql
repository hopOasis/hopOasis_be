CREATE TABLE IF NOT EXISTS beer_rating
(
  id
  BIGINT
  AUTO_INCREMENT
  PRIMARY
  KEY,
  rating
  DOUBLE
  NOT
  NULL,
  beer_id
  INT
  NOT
  NULL,
  CONSTRAINT
  fk_beer_rating
  FOREIGN
  KEY
(
  beer_id
) REFERENCES beer
(
  id
) ON DELETE CASCADE
  );