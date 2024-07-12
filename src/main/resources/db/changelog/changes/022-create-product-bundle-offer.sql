CREATE TABLE IF NOT EXISTS product_bundle_special_offer_product (
     product_bundle_id INT,
     special_offer_product_id INT,
     PRIMARY KEY (product_bundle_id, special_offer_product_id),
     FOREIGN KEY (product_bundle_id) REFERENCES products_bundle(id),
     FOREIGN KEY (special_offer_product_id) REFERENCES special_offer_product(id)
);