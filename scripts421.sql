-- Домашнее задание 4.2, задание 1
-- Ограничения для таблиц student и faculty.

-- Возраст студента не может быть меньше 16 лет,
-- а при отсутствии значения по умолчанию используется 20.
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20,
    ADD CONSTRAINT student_age_check CHECK (age >= 16);

-- Имя студента обязательно и должно быть уникальным.
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT student_name_unique UNIQUE (name);

-- Пара "название факультета + цвет" должна быть уникальной.
ALTER TABLE faculty
    ADD CONSTRAINT faculty_name_color_unique UNIQUE (name, color);
