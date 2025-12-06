CREATE TABLE `sys_user_total`
(
    `name` varchar(255) NOT NULL,
    `type` varchar(255) NOT NULL,
    `num`  int          NOT NULL,
    PRIMARY KEY (`name`, `type`)
);
