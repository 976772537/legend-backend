DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users_roles_rela;

CREATE TABLE users
(
    user_id           VARCHAR(50)  NOT NULL PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL UNIQUE,
    password          VARCHAR(200) NOT NULL,
    nickname          VARCHAR(50)  NOT NULL UNIQUE,
    gender            CHAR         NOT NULL,
    level             INT          NOT NULL DEFAULT 0,
    exp               BIGINT       NOT NULL DEFAULT 0,
    head_image        TEXT         NULL     DEFAULT 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564502581607&di=9a7e7c517c184f5fb7293e3153d7145c&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201609%2F30%2F20160930131229_wjnH2.jpeg',
    email             varchar(20)  NULL,
    create_time_in_ms BIGINT       NOT NULL,
    update_time_in_ms BIGINT       NOT NULL
);

CREATE TABLE roles
(
    role_id   VARCHAR(50) NOT NULL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles_rela
(
    user_id           VARCHAR(50) NOT NULL,
    role_id           VARCHAR(50) NOT NULL,
    create_time_in_ms TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP(),
    update_time_in_ms TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP()
);