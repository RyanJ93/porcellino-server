CREATE TABLE IF NOT EXISTS client_trackings
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    ref_name        VARCHAR(25)  NOT NULL,
    ref_id          BIGINT       NOT NULL,
    user_agent      VARCHAR(256),
    ip_address      VARCHAR(128) NOT NULL,
    browser_name    VARCHAR(64),
    browser_version VARCHAR(16),
    os_name         VARCHAR(64),
    os_version      VARCHAR(16),
    country_name    VARCHAR(64),
    country_code    VARCHAR(8),
    region_name     VARCHAR(64),
    city_name       VARCHAR(64),
    zip_code        VARCHAR(16),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ref_name_ref_id_idx ON client_trackings (ref_name, ref_id);
