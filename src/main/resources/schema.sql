CREATE TABLE IF NOT EXISTS APP_USER
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    username           VARCHAR(50)  NOT NULL,
    `password`         VARCHAR(256) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    role_id            BIGINT       NOT NULL,
    enabled            BIT          NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL
);