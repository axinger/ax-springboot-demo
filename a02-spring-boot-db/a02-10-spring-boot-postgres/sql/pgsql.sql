CREATE TABLE students
(
    id BIGSERIAL PRIMARY KEY,
    name     varchar(50),
    age      int,
    birthday date
);

-- BIGSERIAL int8 8个字节,64位
-- SERIAL int4 4个字节,32位,量小,可以使用,提高性能


INSERT INTO students (name, age, birthday)
VALUES ('Alice', 20, '2022-01-01 12:00:00');


-- CREATE TABLE child_table () INHERITS (parent_table); 继承表
