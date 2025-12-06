/**username和email均有唯一索引，任意一个重复均会触发更新*/
CREATE TABLE `sys_user`
(
    `id`          VARCHAR(255) NOT NULL COMMENT '创建时间',
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `password`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话',
    `age`         int                                                          DEFAULT NULL,
    `info`        json                                                         DEFAULT NULL COMMENT '拓展字段',
    `info_list`   json                                                         DEFAULT NULL,
    `create_time` datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                     DEFAULT NULL COMMENT '更新时间',
    `deleted`     tinyint                                                      DEFAULT '0' COMMENT '逻辑删除0正常,1删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `email` (`email`)
);
