CREATE TABLE personal_area (
    id INT AUTO_INCREMENT PRIMARY KEY,
    delete_area VARCHAR(256) NOT NULL,
    user_id INT,
    user_details_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (user_details_id) REFERENCES user_details(id)
);