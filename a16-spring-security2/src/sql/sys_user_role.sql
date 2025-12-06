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

 Date: 19/07/2025 23:34:07
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          int      NOT NULL AUTO_INCREMENT,
    `user_id`     int      NOT NULL,
    `role_id`     int      NOT NULL,
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`     bigint   NULL DEFAULT 0,
    `is_deleted`  tinyint  NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_role` (`user_id` ASC, `role_id` ASC) USING BTREE,
    INDEX `idx_role_id` (`role_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1, 1, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_user_role`
VALUES (2, 2, 2, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_user_role`
VALUES (3, 3, 3, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_user_role`
VALUES (4, 2, 4, '2025-07-19 17:51:12', '2025-07-19 17:51:12', 0, 0);

SET
    FOREIGN_KEY_CHECKS = 1;
