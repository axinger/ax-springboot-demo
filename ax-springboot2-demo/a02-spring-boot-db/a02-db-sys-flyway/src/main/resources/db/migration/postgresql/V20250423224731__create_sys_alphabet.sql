-- 创建表
CREATE TABLE sys_alphabet
(
    id VARCHAR(255) NOT NULL,
    b  NUMERIC(10, 2) DEFAULT NULL,
    c  TIMESTAMP      DEFAULT NULL,
    d  TIMESTAMP      DEFAULT NULL,
    a  VARCHAR(255)   DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 创建索引（PostgreSQL 不支持 USING BTREE 在 PRIMARY KEY 中，但可以单独创建）
-- 主键会自动创建索引

-- 插入数据
INSERT INTO sys_alphabet (id, b, c, d, a)
VALUES ('1', 2.13, '2025-04-24 20:30:26', '2025-04-23 20:30:29', '王五'),
       ('2', 2.00, NULL, '2025-04-23 20:30:29', 'jim'),
       ('3', 20.00, NULL, NULL, '张三'),
       ('4', 25.00, NULL, NULL, '李四'),
       ('5', 30.00, NULL, NULL, '王五');
