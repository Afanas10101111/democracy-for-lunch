DELETE
FROM restaurants;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', '$2a$12$Cfun/vx7hjCxUbJIb1jw4.dv8emJIrSssiOr79YzHvVNDjUfTjtBy'),
       ('Юзер', 'user@yandex.ru', '$2a$12$Tw99kGYNVRdMmgLaIAz7qeMp13jip4jrVp29UuPeomG97/6U8OoNK');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO restaurants (name, address, voices)
VALUES ('McDonalds', 'Moscow', 0),
       ('BurgerKing', 'Moscow', 0),
       ('KFC', 'Moscow', 0),
       ('SubWay', 'Москва', 0);

INSERT INTO dishes (restaurant_id, name, price, serving_date)
VALUES (100002, 'hamburger', 5000, now()),
       (100002, 'cheeseburger', 6000, now()),
       (100002, 'big mak', 16000, now()),
       (100003, 'king burger', 8000, now()),
       (100003, 'king burger royal', 18000, now()),
       (100004, 'sunders wings', 9000, now()),
       (100004, 'big basket', 19000, now()),
       (100005, 'mega sandwich', 10000, '2020-12-21');
