/*
 Navicat Premium Dump SQL

 Source Server         : mysql-8.0.39
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : 110.42.42.198:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 05/01/2025 15:40:23
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `title`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
    `content`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
    `tags`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签列表（json 数组）',
    `thumbNum`   int      NOT NULL DEFAULT 0 COMMENT '点赞数',
    `favourNum`  int      NOT NULL DEFAULT 0 COMMENT '收藏数',
    `userId`     bigint   NOT NULL COMMENT '创建用户 id',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX        `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for post_favour
-- ----------------------------
DROP TABLE IF EXISTS `post_favour`;
CREATE TABLE `post_favour`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `postId`     bigint   NOT NULL COMMENT '帖子 id',
    `userId`     bigint   NOT NULL COMMENT '创建用户 id',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX        `idx_postId`(`postId` ASC) USING BTREE,
    INDEX        `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_favour
-- ----------------------------

-- ----------------------------
-- Table structure for post_thumb
-- ----------------------------
DROP TABLE IF EXISTS `post_thumb`;
CREATE TABLE `post_thumb`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `postId`     bigint   NOT NULL COMMENT '帖子 id',
    `userId`     bigint   NOT NULL COMMENT '创建用户 id',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX        `idx_postId`(`postId` ASC) USING BTREE,
    INDEX        `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子点赞' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of post_thumb
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`         bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `parentId`   bigint NULL DEFAULT NULL COMMENT '父ID',
    `type`       tinyint NULL DEFAULT 0 COMMENT '资源类型：0：其他 1：菜单 2：按钮',
    `name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源名称',
    `remark`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `sourceType` tinyint NULL DEFAULT 1 COMMENT '源类型：1：内部 2：外部',
    `menuSort`   int NULL DEFAULT NULL COMMENT '菜单排序',
    `menuLevel`  int NULL DEFAULT NULL COMMENT '菜单层级 1级、2级、3级......',
    `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission`
VALUES (1, NULL, NULL, 'console', '后台管理', NULL, NULL, NULL, '2025-01-04 16:36:18', '2025-01-04 16:36:18');
INSERT INTO `sys_permission`
VALUES (2, NULL, NULL, 'space', '空间管理', NULL, NULL, NULL, '2025-01-04 16:36:18', '2025-01-04 16:36:18');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`         bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `roleName`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
    `remark`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `roleType`   tinyint NULL DEFAULT 1 COMMENT '角色类型 1内部 2外部',
    `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, 'admin', '系统管理员', NULL, NULL, NULL);
INSERT INTO `sys_role`
VALUES (2, 'user', '普通用户', NULL, NULL, NULL);
INSERT INTO `sys_role`
VALUES (3, 'spaceadmin', '空间管理员', NULL, NULL, NULL);
INSERT INTO `sys_role`
VALUES (4, 'vip', '会员', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `roleId`       bigint NOT NULL COMMENT '角色ID',
    `permissionId` bigint NOT NULL COMMENT '资源ID',
    `createTime`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX `uk_roleId_permissionId`(`roleId` ASC, `permissionId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission`
VALUES (1, 1, '2025-01-04 16:36:35', '2025-01-04 16:36:35');
INSERT INTO `sys_role_permission`
VALUES (1, 2, '2025-01-04 16:36:35', '2025-01-04 16:36:35');
INSERT INTO `sys_role_permission`
VALUES (3, 2, '2025-01-04 16:36:35', '2025-01-04 16:36:35');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`            bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userAccount`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
    `userPassword`  varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `unionId`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信开放平台id',
    `mpOpenId`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公众号openId',
    `userName`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
    `userAvatar`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
    `userProfile`   varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
    `userRole`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
    `vipNumber`     bigint NULL DEFAULT NULL COMMENT '会员编号',
    `vipCode`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员兑换码',
    `vipExpireTime` datetime NULL DEFAULT NULL COMMENT '会员过期时间',
    `shareCode`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分享码',
    `inviteUser`    bigint NULL DEFAULT NULL COMMENT '邀请用户id',
    `editTime`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `createTime`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`      tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_unionId`(`unionId` ASC) USING BTREE,
    INDEX           `uk_userAccount`(`userAccount` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 'user1', '492a65bef0ab2fac75758f004f3eaf35', 'unionId1', 'mpOpenId1', 'user1',
        'https://api.oss.cqbo.com/tenyon/assets/default.png', '喜欢编程的小白', 'user', NULL, NULL, NULL, NULL, NULL,
        '2024-11-28 14:50:35', '2024-11-28 14:50:35', '2025-01-03 19:57:48', 0);
INSERT INTO `sys_user`
VALUES (2, 'user2', '492a65bef0ab2fac75758f004f3eaf35', 'unionId2', 'mpOpenId2', 'user2',
        'https://api.oss.cqbo.com/tenyon/assets/default.png', '全栈开发工程师', 'user', NULL, NULL, NULL, NULL, NULL,
        '2024-11-28 14:50:35', '2024-11-28 14:50:35', '2025-01-03 19:57:49', 0);
INSERT INTO `sys_user`
VALUES (3, 'user3', '492a65bef0ab2fac75758f004f3eaf35', 'unionId3', 'mpOpenId3', 'user3',
        'https://api.oss.cqbo.com/tenyon/assets/default.png', '前端爱好者', 'user', NULL, NULL, NULL, NULL, NULL,
        '2024-11-28 14:50:35', '2024-11-28 14:50:35', '2025-01-03 19:57:50', 0);
INSERT INTO `sys_user`
VALUES (4, 'user4', '492a65bef0ab2fac75758f004f3eaf35', 'unionId4', 'mpOpenId4', 'user4',
        'https://api.oss.cqbo.com/tenyon/assets/default.png', '后端开发工程师', 'user', NULL, NULL, NULL, NULL, NULL,
        '2024-11-28 14:50:35', '2024-11-28 14:50:35', '2025-01-03 19:57:50', 0);
INSERT INTO `sys_user`
VALUES (5, 'admin', '492a65bef0ab2fac75758f004f3eaf35', NULL, NULL, 'admin123',
        'https://api.oss.cqbo.com/tenyon/assets/default.png', '系统管理员', 'admin', NULL, NULL, NULL, NULL, NULL,
        '2024-11-28 14:50:35', '2024-11-28 14:50:35', '2025-01-03 19:57:51', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `userId`     bigint NOT NULL COMMENT '用户ID',
    `roleId`     bigint NOT NULL COMMENT '角色ID',
    `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX `uk_userId_roleId`(`userId` ASC, `roleId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET
FOREIGN_KEY_CHECKS = 1;
