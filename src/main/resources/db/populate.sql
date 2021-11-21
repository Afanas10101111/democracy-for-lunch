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

INSERT INTO restaurants (name, address)
VALUES ('McDonalds', 'Moscow'),
       ('BurgerKing', 'Moscow'),
       ('KFC', 'Moscow'),
       ('SubWay', 'Moscow');

INSERT INTO meals (restaurant_id, name, price, date)
VALUES (100002, 'hamburger', 50.00, '2021-11-18'),
       (100002, 'cheeseburger', 60.00, '2021-11-18'),
       (100002, 'big mak', 160.00, '2021-11-18'),
       (100003, 'king burger', 80.00, '2021-11-18'),
       (100003, 'king burger royal', 180.00, '2021-11-18'),
       (100004, 'sunders wings', 90.00, '2021-11-18'),
       (100004, 'big basket', 190.00, '2021-11-18'),
       (100005, 'mega sandwich', 100.00, '2021-11-18');
