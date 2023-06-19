/*
 Navicat Premium Data Transfer

 Source Server         : 本地-mysql-67
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 172.21.0.67:3306
 Source Schema         : ddd_test

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 21/03/2023 20:31:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_uc_login
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_login`;
CREATE TABLE `t_uc_login`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `login_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名 / 登录名 / 真实姓名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户登录密码',
  `auth_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '授权类型',
  `open_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户 微信 小程序唯一标识  open id',
  `union_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户在微信开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回',
  `role_id` int NOT NULL DEFAULT -1 COMMENT '角色 id',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `i_uc_user_union_id`(`union_id`) USING BTREE,
  INDEX `i_uc_user_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000001 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户登录信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_uc_login
-- ----------------------------
INSERT INTO `t_uc_login` VALUES (1000000, '18012663434', 'Johnson', 'e10adsdfc3sdfsdfsdf0f883e', 'WECHAT', 'oFRpA5WsdfsdfubE3mVYJNqd4', 'oM5Okt-CN-pH6rYPkrdU_BWiIR9s', 1, 1595236695269);

-- ----------------------------
-- Table structure for t_uc_role
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role`;
CREATE TABLE `t_uc_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色 名称',
  `menus` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色 拥有 菜单 ( 功能 )  多个英文逗号分割 “,”',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_uc_role
-- ----------------------------
INSERT INTO `t_uc_role` VALUES (1, '系统管理员', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96', '2022-11-27 17:00:44');
INSERT INTO `t_uc_role` VALUES (2, '普通管理员', '1,2,3,4,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,94,95,96', '2022-11-27 17:00:44');
INSERT INTO `t_uc_role` VALUES (3, '物流发货员', '9,20,76,77,82,88,89,90,91,92,93,94', '2022-11-27 17:00:44');
INSERT INTO `t_uc_role` VALUES (4, '只读账户', '', '2022-11-27 17:00:44');

-- ----------------------------
-- Table structure for t_uc_role_menus
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_role_menus`;
CREATE TABLE `t_uc_role_menus`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '菜单或功能id',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '菜单 或 功能点名称',
  `url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '请求访问 url ',
  `interface_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '后端 接口url',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '未选中状态 菜单icon 图标 ',
  `re_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '选中状态 菜单icon图标',
  `parent_id` int NOT NULL DEFAULT -1 COMMENT '父级 菜单或功能 id',
  `type` int NOT NULL DEFAULT -1 COMMENT '菜单 类型    0：菜单   1：功能点 按钮等',
  `code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '功能编码 对应前端编号',
  `status` int NOT NULL DEFAULT 0 COMMENT '菜单状态    0：隐藏  1：开启',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_uc_role_menus
-- ----------------------------
INSERT INTO `t_uc_role_menus` VALUES (1, '资讯管理', '/admin/banner', '', 'el-icon-news', '', 0, 0, '00001', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (2, '游戏管理', '/admin/game', '', 'el-icon-trophy-1', '', 0, 0, '00002', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (3, '抽奖管理', '/admin/activity', '', 'el-icon-present', '', 0, 0, '00003', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (4, '用户管理', '/admin/user', '', 'el-icon-user-solid', '', 0, 0, '00004', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (5, '用户抽奖管理', '/admin/userActivity', '', 'el-icon-s-management', '', 0, 0, '00005', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (6, '盲盒商品管理', '/admin/goods', '', 'el-icon-s-goods', '', 0, 0, '00006', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (7, 'NFT商品管理', '/admin/nftGoods', '', 'el-icon-s-goods', '', 0, 0, '00007', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (8, '买家秀管理', '/admin/show', '', 'el-icon-s-comment', '', 0, 0, '00008', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (9, '订单管理', '/admin/order', '', 'el-icon-s-order', '', 0, 0, '00009', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (10, '首页', '/admin/index', '', 'el-icon-s-home', '', 0, 0, '00010', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (11, '权限管理', 'authAdmin', '', 'el-icon-trophy-1', '', 0, 0, '00011', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (12, '管理员管理', 'authAdminUser', '', 'el-icon-trophy-1', '', 11, 0, '11012', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (13, '管理员日志', 'authAdminUserLog', '', 'el-icon-trophy-1', '', 11, 0, '11013', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (14, '菜单管理', 'authMenus', '', 'el-icon-trophy-1', '', 11, 0, '11014', 1, '2022-11-27 11:15:55');

-- ----------------------------
-- Table structure for t_uc_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_uc_user_info`;
CREATE TABLE `t_uc_user_info`  (
  `user_id` bigint NOT NULL DEFAULT -1 COMMENT '用户id',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `country` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户 所在省份',
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户所在城市',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户详细地址',
  `gender` int NOT NULL DEFAULT 0 COMMENT '用户性别  1：男性    0：女性',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户头像 地址',
  `identity_card` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '身份证 号码',
  `face_token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '百度云人脸face token 标识',
  `face_image_url` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '人脸识别图片url列表 多张 \";\" 分号分割。 上传到 阿里云oss',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间  时间戳',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `i_uc_user_info_nick`(`nick_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_uc_user_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
