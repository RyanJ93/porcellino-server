CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(256) NOT NULL,
    password   VARCHAR(256) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE UNIQUE INDEX users_email_idx ON users (email);

CREATE TABLE IF NOT EXISTS currencies
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    code   VARCHAR(10) NOT NULL,
    name   VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL
);
CREATE UNIQUE INDEX currencies_code_idx ON currencies (code);
