-- 创建表
CREATE TABLE sys_user
(
    id          VARCHAR(255) NOT NULL,
    username    VARCHAR(50) DEFAULT NULL,
    password    VARCHAR(50) DEFAULT NULL,
    email       VARCHAR(50) DEFAULT NULL,
    phone       VARCHAR(20) DEFAULT NULL,
    age         INTEGER     DEFAULT NULL,
    info        JSON        DEFAULT NULL,
    info_list   JSON        DEFAULT NULL,
    create_time TIMESTAMP   DEFAULT NULL,
    update_time TIMESTAMP   DEFAULT NULL,
    deleted     SMALLINT    DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE (username),
    UNIQUE (email)
);

-- 添加列注释（必须单独执行）
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '电话';
COMMENT ON COLUMN sys_user.info IS '拓展字段';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.deleted IS '逻辑删除0正常,1删除';
