CREATE TABLE IF NOT EXISTS payment_data (
    user_details_id INT,
    card_number INTEGER NOT NULL,
    cvv INTEGER NOT NULL,
    expiry_date DATE NOT NULL,
    FOREIGN KEY (user_details_id) REFERENCES user_details(id)
    ON UPDATE CASCADE ON DELETE CASCADE
);