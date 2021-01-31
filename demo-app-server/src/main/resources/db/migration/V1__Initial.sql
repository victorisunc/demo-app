CREATE TABLE IF NOT EXISTS mobile_customer
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    company_name VARCHAR(100),
    email        VARCHAR(100) NOT NULL,
    create_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mobile_ad
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    make               VARCHAR(100) NOT NULL,
    model              VARCHAR(100) NOT NULL,
    description        TEXT,
    category           VARCHAR(100) NOT NULL,
    price              DECIMAL      NOT NULL,
    mobile_customer_id BIGINT       NOT NULL,
    create_time        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mobile_customer
        FOREIGN KEY (mobile_customer_id) REFERENCES mobile_customer (id) ON DELETE CASCADE
);

