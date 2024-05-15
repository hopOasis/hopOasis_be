CREATE TABLE payment_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_details_id INT,
    card_number INTEGER NOT NULL,
    cvv INTEGER NOT NULL,
    card_date DATE NOT NULL,
    FOREIGN KEY (user_details_id) REFERENCES user_details(id)
);