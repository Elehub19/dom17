-- Домашнее задание 4.2, задание 2
-- У одного автомобиля может быть несколько пользователей.

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    cost DECIMAL(12, 2) NOT NULL
);

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_driver_license BOOLEAN NOT NULL,
    car_id BIGINT NOT NULL,
    CONSTRAINT person_car_fk
        FOREIGN KEY (car_id) REFERENCES car(id)
);
