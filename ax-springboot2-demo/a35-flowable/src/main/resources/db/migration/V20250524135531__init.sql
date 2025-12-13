CREATE TABLE `department`
(
    `id`                varchar(36)  NOT NULL COMMENT '部门ID，UUID格式的唯一标识符',
    `name`              varchar(100) NOT NULL COMMENT '部门名称，如"技术部"、"市场部"',
    `dept_leader_id`    varchar(36) DEFAULT NULL COMMENT '部门领导ID，关联employee表的id',
    `company_leader_id` varchar(36) DEFAULT NULL COMMENT '公司领导ID，关联employee表的id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门信息表';


CREATE TABLE `employee`
(
    `id`               varchar(36)  NOT NULL COMMENT '员工ID，UUID格式的唯一标识符',
    `name`             varchar(100) NOT NULL COMMENT '员工姓名',
    `position`         varchar(50)  NOT NULL COMMENT '员工职位，如"初级开发"、"技术经理"',
    `department_id`    varchar(36)  NOT NULL COMMENT '所属部门ID，关联department表的id',
    `direct_leader_id` varchar(36) DEFAULT NULL COMMENT '直属领导ID，关联本表的id(自关联)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='员工信息表';


-- 插入初始部门数据  COMMENT '公司领导统一为e003',COMMENT 'CEO没有直属领导'
INSERT INTO `department` (`id`, `name`, `dept_leader_id`, `company_leader_id`)
VALUES ('d001', '技术部', 'e002', 'e003'),
       ('d002', '市场部', 'e004', 'e003'),
       ('d003', '人事部', NULL, 'e003');


-- 插入初始员工数据
INSERT INTO `employee` (`id`, `name`, `position`, `department_id`, `direct_leader_id`)
VALUES ('e001', '张三', '初级开发', 'd001', 'e002'),
       ('e002', '李四', '技术经理', 'd001', 'e003'),
       ('e003', '王五', 'CEO', 'd003', NULL),
       ('e004', '赵六', '市场总监', 'd002', 'e003');
