CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT        NOT NULL,
    token      VARCHAR(4096) NOT NULL,
    scopes     VARCHAR(4096)          DEFAULT NULL,
    payload    TEXT                   DEFAULT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP     NOT NULL DEFAULT NOW(),
    expired_at TIMESTAMP,
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
