CREATE TABLE user
(
    id                      BIGINT              PRIMARY KEY AUTO_INCREMENT,
    email                   VARCHAR(255)        NOT NULL,
    username                VARCHAR(255)        NOT NULL,
    password                VARCHAR(255)        NOT NULL,
    login_issuer            VARCHAR(255)        NOT NULL,
    account_enabled         BOOLEAN             NOT NULL,
    account_expired         BOOLEAN             NOT NULL,
    account_locked          BOOLEAN             NOT NULL,
    credentials_expired     BOOLEAN             NOT NULL,
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE role
(
    id                      BIGINT              PRIMARY KEY AUTO_INCREMENT,
    user_id                 BIGINT              NOT NULL,
    role                    VARCHAR(255)        NOT NULL,
    created_at              DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES user (id)
);