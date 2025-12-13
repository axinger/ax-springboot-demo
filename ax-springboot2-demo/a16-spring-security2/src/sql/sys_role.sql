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

 Date: 19/07/2025 23:33:36
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT,
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `code`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '角色编码',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `status`      tinyint                                                       NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`     bigint                                                        NULL DEFAULT 0,
    `is_deleted`  tinyint                                                       NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_name` (`name` ASC) USING BTREE,
    UNIQUE INDEX `idx_code` (`code` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'ROLE_ADMIN', '拥有所有权限', 1, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role`
VALUES (2, '普通用户', 'ROLE_USER', '普通用户权限', 1, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role`
VALUES (3, '访客', 'ROLE_GUEST', '只读权限', 1, '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_role`
VALUES (4, '部门领导', 'ROLE_LEADER', NULL, 1, '2025-07-19 17:50:56', '2025-07-19 17:50:56', 0, 0);

SET
    FOREIGN_KEY_CHECKS = 1;
