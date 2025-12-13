-- 用户表
CREATE TABLE sys_users
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(100) NOT NULL COMMENT '密码',
    email       VARCHAR(100) COMMENT '邮箱',
    real_name   VARCHAR(50) COMMENT '真实姓名',
    department  VARCHAR(50) COMMENT '所属部门',
    position    VARCHAR(50) COMMENT '职位',
    status      TINYINT    DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted     TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- 角色表
CREATE TABLE sys_roles
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name        VARCHAR(50) NOT NULL COMMENT '角色名称',
    code        VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    sort_order  INT        DEFAULT 0 COMMENT '排序',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted     TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- API表
CREATE TABLE sys_apis
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'API ID',
    path         VARCHAR(255) NOT NULL COMMENT 'API路径',
    method       VARCHAR(10)  NOT NULL COMMENT 'HTTP方法',
    service_name VARCHAR(100) COMMENT '服务名称',
    description  VARCHAR(255) COMMENT 'API描述',
    category     VARCHAR(50) COMMENT '分类',
    enabled      TINYINT    DEFAULT 1 COMMENT '是否启用：0-否，1-是',
    create_time  DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version      INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted      TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除',
    UNIQUE KEY unique_api (path, method)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='API表';

-- 权限表
CREATE TABLE sys_permissions
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    name        VARCHAR(100) NOT NULL COMMENT '权限名称',
    code        VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    description VARCHAR(255) COMMENT '权限描述',
    category    VARCHAR(50) COMMENT '分类',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted     TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

-- 用户-角色关联表
CREATE TABLE sys_user_roles
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    role_id     BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version     INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted     TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除',
    UNIQUE KEY unique_user_role (user_id, role_id, deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- 角色-权限关联表
CREATE TABLE sys_role_permissions
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id       BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time   DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version       INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted       TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除',
    UNIQUE KEY unique_role_permission (role_id, permission_id, deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色权限关联表';

-- 权限-API关联表
CREATE TABLE sys_permission_apis
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    api_id        BIGINT NOT NULL COMMENT 'API ID',
    create_time   DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version       INT        DEFAULT 0 COMMENT '版本号，用于乐观锁',
    deleted       TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标记，0-未删除，1-已删除',
    UNIQUE KEY unique_permission_api (permission_id, api_id, deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限API关联表';
