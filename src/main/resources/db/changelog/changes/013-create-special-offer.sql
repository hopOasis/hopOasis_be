CREATE TABLE IF NOT EXIST special_offer_product(
    Id INT  PRIMARY KEY AUTO_INCREMENT,
    beer_id INT,
    cider_id INT,
    snack_id INT,
    product_bundle_id INT,
    CONSTRAINT fk_beer FOREIGN KEY (beer_id) REFERENCES beer(id) ON DELETE SET NULL,
    CONSTRAINT fk_cider FOREIGN KEY (cider_id) REFERENCES cider(id) ON DELETE SET NULL,
    CONSTRAINT fk_snack FOREIGN KEY (snack_id) REFERENCES snack(id) ON DELETE SET NULL,
    CONSTRAINT fk_product_bundle FOREIGN KEY (product_bundle_id) REFERENCES product_bundle(id) ON DELETE SET NULL
)