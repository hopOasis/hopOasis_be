CREATE TABLE IF NOT EXISTS review_reactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reaction VARCHAR(50),
    review_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uc_user_review UNIQUE (user_id, review_id)


)