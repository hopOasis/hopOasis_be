CREATE TABLE IF NOT EXISTS snack_special_offer_product (
    snack_id INT,
    special_offer_product_id INT,
    PRIMARY KEY (snack_id, special_offer_product_id),
    FOREIGN KEY (snack_id) REFERENCES snacks(id),
    FOREIGN KEY (special_offer_product_id) REFERENCES special_offer_product(id)
);
