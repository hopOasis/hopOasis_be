CREATE TABLE IF NOT EXISTS team_recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    item_type ENUM('BEER', 'CIDER') NOT NULL,
    author_name VARCHAR(50) NOT NULL,
    team_role VARCHAR(50) NOT NULL,
    text_recommendation VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP



);