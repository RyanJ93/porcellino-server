CREATE TABLE IF NOT EXISTS recovery_codes
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id        INT       NOT NULL,
    code           VARCHAR(256) NOT NULL,
    created_at     TIMESTAMP DEFAULT NOW(),
    invalidated_at TIMESTAMP    DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
