CREATE TABLE IF NOT EXISTS cider_special_offer_product (
     cider_id INT,
     special_offer_product_id INT,
     PRIMARY KEY (cider_id, special_offer_product_id),
     FOREIGN KEY (cider_id) REFERENCES cider(id),
     FOREIGN KEY (special_offer_product_id) REFERENCES special_offer_product(id)
);
