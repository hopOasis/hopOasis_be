    CREATE TABLE IF NOT EXISTS personal_area (
        delete_area BOOLEAN,
        user_id INT,
        user_details_id INT,
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (user_details_id) REFERENCES user_details(id)
        ON UPDATE CASCADE ON DELETE CASCADE
    );