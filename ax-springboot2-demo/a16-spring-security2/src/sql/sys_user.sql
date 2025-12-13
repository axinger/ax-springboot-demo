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

 Date: 19/07/2025 23:34:01
*/

SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT,
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `email`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `phone`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL,
    `status`      tinyint                                                       NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`     bigint                                                        NULL DEFAULT 0,
    `is_deleted`  tinyint                                                       NULL DEFAULT 0 COMMENT '0-未删除 1-已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_username` (`username` ASC) USING BTREE,
    INDEX `idx_status` (`status` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 'admin', '$2a$10$awyqdVCX6dq/7FTaHd3BcuwRn.pdiVsfdgg9HWsiLS/1MJH2Fp/uS', 'admin@example.com', '13800138000',
        1, '2025-07-12 23:09:11', '2025-07-13 21:27:04', 0, 0);
INSERT INTO `sys_user`
VALUES (2, 'jim', '$2a$10$awyqdVCX6dq/7FTaHd3BcuwRn.pdiVsfdgg9HWsiLS/1MJH2Fp/uS', 'jim@example.com', '13800138001', 1,
        '2025-07-12 23:09:11', '2025-07-13 18:25:06', 0, 0);
INSERT INTO `sys_user`
VALUES (3, 'tom', '$2a$10$awyqdVCX6dq/7FTaHd3BcuwRn.pdiVsfdgg9HWsiLS/1MJH2Fp/uS', 'tom@example.com', '13800138002', 1,
        '2025-07-12 23:09:11', '2025-07-19 17:13:54', 0, 0);

SET
    FOREIGN_KEY_CHECKS = 1;
