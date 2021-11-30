DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id           BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    name         VARCHAR                          NOT NULL,
    email        VARCHAR                          NOT NULL,
    password     VARCHAR                          NOT NULL,
    registered   TIMESTAMP          DEFAULT now() NOT NULL,
    enabled      BOOL               DEFAULT TRUE  NOT NULL,
    vote_date    DATE,
    voted_for_id INTEGER
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id      BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    name    VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    voices  INTEGER NOT NULL,
    CONSTRAINT restaurants_unique_name_address_idx UNIQUE (name, address)
);

CREATE TABLE meals
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    restaurant_id BIGINT  NOT NULL,
    name          VARCHAR NOT NULL,
    price         NUMERIC NOT NULL,
    created       DATE    NOT NULL,
    CONSTRAINT restaurant_meal_idx UNIQUE (restaurant_id, name),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
