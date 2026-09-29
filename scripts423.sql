-- Домашнее задание 4.2, задание 3

-- 1. Все студенты школы Хогвартс вместе с названиями факультетов.
SELECT s.name, s.age, f.name AS faculty
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id;

-- 2. Только студенты, у которых есть аватарки.
SELECT s.name
FROM student s
INNER JOIN avatar a ON a.student_id = s.id;
