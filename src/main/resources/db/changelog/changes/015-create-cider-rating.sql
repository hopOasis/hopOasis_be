CREATE TABLE IF NOT EXISTS cider_rating
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
  cider_id
  INT
  NOT
  NULL,
  CONSTRAINT
  fk_cider_rating
  FOREIGN
  KEY
(
  cider_id
) REFERENCES cider
(
  id
) ON DELETE CASCADE
  );