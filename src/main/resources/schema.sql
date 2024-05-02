CREATE TABLE IF NOT EXISTS APP_USER
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    phone      VARCHAR(9)   NULL,
    email      VARCHAR(255) NOT NULL,
    `password` VARCHAR(256) NOT NULL
);