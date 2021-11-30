DELETE
FROM restaurants;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin'),
       ('User', 'user@yandex.ru', 'password');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO restaurants (name, address, voices)
VALUES ('McDonalds', 'Moscow', 0),
       ('BurgerKing', 'Moscow', 0),
       ('KFC', 'Moscow', 0),
       ('SubWay', 'Moscow', 0);

INSERT INTO meals (restaurant_id, name, price, created)
VALUES (100002, 'hamburger', 50.00, now()),
       (100002, 'cheeseburger', 60.00, now()),
       (100002, 'big mak', 160.00, now()),
       (100003, 'king burger', 80.00, now()),
       (100003, 'king burger royal', 180.00, now()),
       (100004, 'sunders wings', 90.00, now()),
       (100004, 'big basket', 190.00, now()),
       (100005, 'mega sandwich', 100.00, '2020-12-21');
