CREATE TABLE IF NOT EXISTS users
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    first_name
    VARCHAR
(
    50
) NOT NULL,
    last_name VARCHAR
(
    50
) NOT NULL,
    email VARCHAR
(
    50
) NOT NULL,
    password VARCHAR
(
    50
) NOT NULL
    role VARCHAR
(
    50
) NOT NULL CHECK
(
    role
    IN
(
    'ADMIN',
    'USER'
))
    );