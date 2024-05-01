CREATE TABLE IF NOT EXISTS transactions
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id INT                NOT NULL,
    amount       FLOAT              NOT NULL,
    quantity     INT                NOT NULL DEFAULT 1,
    type         VARCHAR(25) NOT NULL,
    note         TEXT,
    date         TIMESTAMP          NOT NULL DEFAULT NOW(),
    created_at   TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP          NOT NULL DEFAULT NOW(),
    CONSTRAINT FOREIGN KEY (portfolio_id) REFERENCES portfolios (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (type) REFERENCES transaction_types (name) ON DELETE CASCADE
);