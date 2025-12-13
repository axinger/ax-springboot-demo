-- 创建表（无注释）
CREATE TABLE sys_person
(
    id          VARCHAR(255) NOT NULL,
    tenant_id   VARCHAR(255)     DEFAULT NULL,
    name        VARCHAR(255)     DEFAULT NULL,
    age         INTEGER          DEFAULT NULL,
    birthday    TIMESTAMP(6)     DEFAULT NULL,
    gender      INTEGER          DEFAULT NULL,
    height      DOUBLE PRECISION DEFAULT NULL,
    weight      DOUBLE PRECISION DEFAULT NULL,
    create_time TIMESTAMP(6)     DEFAULT NULL,
    update_time TIMESTAMP(6)     DEFAULT NULL,
    version     BIGINT           DEFAULT NULL,
    deleted     INTEGER          DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 单独添加列注释
COMMENT ON COLUMN sys_person.age IS '年龄';
COMMENT ON COLUMN sys_person.birthday IS '生日';
COMMENT ON COLUMN sys_person.create_time IS '创建时间';
COMMENT ON COLUMN sys_person.deleted IS '逻辑删除';
COMMENT ON COLUMN sys_person.gender IS '性别';
COMMENT ON COLUMN sys_person.height IS '身高';
COMMENT ON COLUMN sys_person.name IS '姓名';
COMMENT ON COLUMN sys_person.tenant_id IS '多租户id';
COMMENT ON COLUMN sys_person.update_time IS '更新时间';
COMMENT ON COLUMN sys_person.version IS '版本号';
COMMENT ON COLUMN sys_person.weight IS '体重';

-- 插入数据（注意：id必须是字符串类型，因为定义为VARCHAR）
INSERT INTO sys_person (id, age, birthday, create_time, deleted, gender, height, name, tenant_id,
                        update_time, version, weight)
VALUES ('1', 13, '2015-12-27 06:51:48', '2016-02-04 07:56:05', 0, 783, 896.14, '张三', '1', '2017-04-16 16:09:35', 1,
        897.22),
       ('2', 17, '2016-03-30 14:21:26', '2015-12-30 09:56:38', 0, 718, 106.92, '李四', '1', '2016-03-26 00:26:50', 1,
        826.45),
       ('3', 12, '2009-09-25 10:45:50', '2008-05-20 23:41:36', 0, 295, 378.06, '王五', '1', '2018-03-25 07:18:34', 1,
        815.71),
       ('4', 16, '2008-06-26 00:42:39', '2009-04-25 17:19:34', 0, 373, 682.69, '赵六', '2', '2014-04-26 02:25:43', 1,
        740.9),
       ('5', 20, '2008-11-06 22:35:42', '2007-12-12 17:17:02', 0, 674, 797.13, '田七', '2', '2004-12-28 16:20:53', 1,
        900.25);
