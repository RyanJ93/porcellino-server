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
