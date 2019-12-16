DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users_roles_rela;

CREATE TABLE users
(
    user_id           VARCHAR(50)  NOT NULL PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL UNIQUE,
    password          VARCHAR(200) NOT NULL,
    nickname          VARCHAR(50)  NOT NULL UNIQUE,
    gender            VARCHAR(5)   NOT NULL,
    level             INT          NOT NULL DEFAULT 0,
    exp               BIGINT       NOT NULL DEFAULT 0,
    head_image        TEXT         NOT NULL,
    email             varchar(20)  NOT NULL,
    create_time_in_ms BIGINT       NOT NULL,
    update_time_in_ms BIGINT       NOT NULL
);

CREATE TABLE roles
(
    role_id VARCHAR(50) NOT NULL PRIMARY KEY,
    type    VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles_rela
(
    user_id           VARCHAR(50) NOT NULL,
    role_id           VARCHAR(50) NOT NULL,
    create_time_in_ms TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time_in_ms TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP()
);