-- Домашнее задание 3.4. Введение в SQL
-- Запросы ниже выполнялись для таблицы student (сущность Student),
-- созданной автоматически Hibernate (spring.jpa.hibernate.ddl-auto=update).

-- 1. Получить всех студентов, возраст которых находится между 10 и 20
SELECT *
FROM student
WHERE age BETWEEN 10 AND 20;

-- 2. Получить всех студентов, но отобразить только список их имен
SELECT name
FROM student;

-- 3. Получить всех студентов, у которых в имени присутствует буква "о"
-- (ILIKE — регистронезависимый LIKE в PostgreSQL)
SELECT *
FROM student
WHERE name ILIKE '%о%';

-- 4. Получить всех студентов, у которых возраст меньше идентификатора
SELECT *
FROM student
WHERE age < id;

-- 5. Получить всех студентов, упорядоченных по возрасту
SELECT *
FROM student
ORDER BY age;
