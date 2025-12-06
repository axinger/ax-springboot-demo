CREATE TABLE `sys_person`
(
    `id`          VARCHAR(255) NOT NULL,
    `tenant_id`   varchar(255) DEFAULT NULL COMMENT '多租户id',
    `name`        varchar(255) DEFAULT NULL COMMENT '姓名',
    `age`         int          DEFAULT NULL COMMENT '年龄',
    `birthday`    datetime(6)  DEFAULT NULL COMMENT '生日',
    `gender`      int          DEFAULT NULL COMMENT '性别',
    `height`      double       DEFAULT NULL COMMENT '身高',
    `weight`      double       DEFAULT NULL COMMENT '体重',
    `create_time` datetime(6)  DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(6)  DEFAULT NULL COMMENT '更新时间',
    `version`     bigint       DEFAULT NULL COMMENT '版本号',
    `deleted`     int          DEFAULT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
)
;
INSERT INTO `sys_person` (`id`, `age`, `birthday`, `create_time`, `deleted`, `gender`, `height`, `name`, `tenant_id`,
                          `update_time`, `version`, `weight`)
VALUES (1, 13, '2015-12-27 06:51:48', '2016-02-04 07:56:05', 0, 783, 896.14, '张三', '1', '2017-04-16 16:09:35', 1,
        897.22),
       (2, 17, '2016-03-30 14:21:26', '2015-12-30 09:56:38', 0, 718, 106.92, '李四', '1', '2016-03-26 00:26:50', 1,
        826.45),
       (3, 12, '2009-09-25 10:45:50', '2008-05-20 23:41:36', 0, 295, 378.06, '王五', '1', '2018-03-25 07:18:34', 1,
        815.71),
       (4, 16, '2008-06-26 00:42:39', '2009-04-25 17:19:34', 0, 373, 682.69, '赵六', '2', '2014-04-26 02:25:43', 1,
        740.9),
       (5, 20, '2008-11-06 22:35:42', '2007-12-12 17:17:02', 0, 674, 797.13, '田七', '2', '2004-12-28 16:20:53', 1,
        900.25);
