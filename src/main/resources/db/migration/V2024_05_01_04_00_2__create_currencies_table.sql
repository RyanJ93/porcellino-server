CREATE TABLE IF NOT EXISTS currencies
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    code   VARCHAR(10) NOT NULL,
    name   VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL
);

CREATE UNIQUE INDEX currencies_code_idx ON currencies (code);
