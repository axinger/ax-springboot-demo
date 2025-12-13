CREATE TABLE sys_department
(
    id   VARCHAR(255) NOT NULL,
    name VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO sys_department (id, name)
VALUES (1, '行政部');
INSERT INTO sys_department (id, name)
VALUES (2, '采购部');
