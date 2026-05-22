-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`
(
    `id`         INT NOT NULL COMMENT '主键',
    `name`       VARCHAR(255) COMMENT '姓名',
    `age`        INT COMMENT '年龄',
    `sex`        VARCHAR(255) COMMENT '性别',
    `class_room` VARCHAR(255) COMMENT '班级',
    `address`    VARCHAR(255) COMMENT '家庭住址',
    PRIMARY KEY (`id`)
) COMMENT ='学生表' ENGINE = InnoDB
                    DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student`
VALUES (1, '张三', 18, '男', '三年一班', '山东省济南市高新区'),
       (2, '李四', 20, '女', '三年一班', '山东省济南市高新区'),
       (3, '王五', 18, '男', '三年二班', '山东省济南市高新区'),
       (4, '张三', 17, '女', '三年三班', '山东省济南市高新区'),
       (5, '钱六', 15, '男', '三年三班', '山东省济南市高新区');
