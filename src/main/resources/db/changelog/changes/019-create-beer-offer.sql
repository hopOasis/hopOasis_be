CREATE TABLE beer_special_offer_product (
                                            beer_id BIGINT,
                                            special_offer_product_id BIGINT,
                                            PRIMARY KEY (beer_id, special_offer_product_id),
                                            FOREIGN KEY (beer_id) REFERENCES beer(id),
                                            FOREIGN KEY (special_offer_product_id) REFERENCES special_offer_product(id)
);
