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

 Date: 19/07/2025 23:33:28
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT,
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码(角色或权限标识)',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `type`        tinyint                                                       NULL DEFAULT 1 COMMENT '1-菜单 2-按钮 3-API',
    `parent_id`   int                                                           NULL DEFAULT 0 COMMENT '父权限ID',
    `path`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问路径',
    `method`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'HTTP方法(*表示所有)',
    `icon`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '图标',
    `sort`        int                                                           NULL DEFAULT 0 COMMENT '排序',
    `status`      tinyint                                                       NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`     bigint                                                        NULL DEFAULT 0,
    `is_deleted`  tinyint                                                       NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_code` (`code` ASC) USING BTREE,
    INDEX `idx_parent_id` (`parent_id` ASC) USING BTREE,
    INDEX `idx_type` (`type` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission`
VALUES (1, '用户管理', 'system:user', '用户管理菜单', 1, 0, '/system/user', NULL, 'user', 1, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (2, '用户查询', 'user:read', '查看用户信息', 2, 1, '/system/user/read', NULL, '', 1, 1, '2025-07-12 23:09:11',
        '2025-07-13 21:48:16', 0, 0);
INSERT INTO `sys_permission`
VALUES (3, '用户新增', 'user:create', '新增用户', 2, 1, '', NULL, '', 2, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (4, '用户编辑', 'user:update', '编辑用户', 2, 1, '/system/user/update', NULL, '', 3, 1, '2025-07-12 23:09:11',
        '2025-07-19 16:59:22', 0, 0);
INSERT INTO `sys_permission`
VALUES (5, '用户删除', 'user:delete', '删除用户', 2, 1, '/system/user/delete', NULL, '', 4, 1, '2025-07-12 23:09:11',
        '2025-07-13 21:48:56', 0, 0);
INSERT INTO `sys_permission`
VALUES (6, '角色管理', 'system:role', '角色管理菜单', 1, 0, '/system/role', NULL, 'team', 2, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (7, '角色查询', 'role:read', '查看角色信息', 2, 6, '', NULL, '', 1, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (8, '权限管理', 'system:permission', '权限管理菜单', 1, 0, '/system/permission', NULL, 'lock', 3, 1,
        '2025-07-12 23:09:11', '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (9, '权限查询', 'permission:read', '查看权限信息', 2, 8, '', NULL, '', 1, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);
INSERT INTO `sys_permission`
VALUES (10, '数据导出', 'data:export', '导出数据', 2, 0, '', NULL, '', 5, 1, '2025-07-12 23:09:11',
        '2025-07-12 23:09:11', 0, 0);

SET
    FOREIGN_KEY_CHECKS = 1;
