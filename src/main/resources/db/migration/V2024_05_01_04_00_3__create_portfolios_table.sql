CREATE TABLE IF NOT EXISTS portfolios
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT         NOT NULL,
    currency_id INT         NOT NULL,
    name        VARCHAR(50) NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (currency_id) REFERENCES currencies (id)
);
