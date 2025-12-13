CREATE TABLE `sys_employee`
(
    `id`      VARCHAR(255) NOT NULL,
    `dept_id` VARCHAR(255) DEFAULT NULL,
    `email`   varchar(255) DEFAULT NULL,
    `gender`  int          DEFAULT NULL,
    `name`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `sys_employee` (`id`, `dept_id`, `email`, `gender`, `name`)
VALUES (1, 1, NULL, 1, '张三'),
       (2, 1, NULL, 1, '李四'),
       (3, 2, NULL, 1, '王五');
