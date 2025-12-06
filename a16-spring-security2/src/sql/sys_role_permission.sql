/*
 Navicat Premium Dump SQL

 Source Server         : hadoop102
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : hadoop102:3306
 Source Schema         : ax_test16

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 19/07/2025 23:33:55
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            int      NOT NULL AUTO_INCREMENT,
    `role_id`       int      NOT NULL,
    `permission_id` int      NOT NULL,
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`       bigint   NULL DEFAULT 0,
    `is_deleted`    tinyint  NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role_permission` (`role_id` ASC, `permission_id` ASC) USING BTREE,
    INDEX `idx_permission_id` (`permission_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission`
VALUES (1, 1, 1, '2025-07-12 23:09:11', '2025-07-19 16:53:43', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (2, 1, 2, '2025-07-12 23:09:11', '2025-07-19 16:53:55', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (3, 1, 3, '2025-07-12 23:09:11', '2025-07-19 16:53:56', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (4, 1, 4, '2025-07-12 23:09:11', '2025-07-19 16:53:56', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (5, 1, 5, '2025-07-12 23:09:11', '2025-07-19 16:53:56', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (6, 1, 6, '2025-07-12 23:09:11', '2025-07-19 16:53:56', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (7, 1, 7, '2025-07-12 23:09:11', '2025-07-19 16:53:56', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (8, 1, 8, '2025-07-12 23:09:11', '2025-07-19 16:53:57', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (9, 1, 9, '2025-07-12 23:09:11', '2025-07-19 16:53:57', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (10, 1, 10, '2025-07-12 23:09:11', '2025-07-19 16:53:58', 0, 1);
INSERT INTO `sys_role_permission`
VALUES (11, 2, 1, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (12, 2, 2, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (13, 2, 6, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (14, 2, 7, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (15, 2, 10, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (16, 3, 2, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (17, 3, 7, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (18, 3, 9, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role_permission`
VALUES (19, 2, 4, '2025-07-19 16:58:13', '2025-07-19 16:58:13', 0, 0);

SET
    FOREIGN_KEY_CHECKS = 1;
