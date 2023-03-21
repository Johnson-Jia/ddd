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
INSERT INTO `t_uc_login` VALUES (1000000, '18012663434', 'Johnson', 'e10adc3949ba59abbe56e057f20f883e', 'WECHAT', 'oFRpA5WZ6FUvHtFMubE3mVYJNqd4', 'oM5Okt-CN-pH6rYPkrdU_BWiIR9s', 1, 1595236695269);

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
INSERT INTO `t_uc_role_menus` VALUES (15, '预留菜单', '', '', '', '', 0, 0, '', 0, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (16, '预留菜单', '', '', '', '', 0, 0, '', 0, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (17, '预留菜单', '', '', '', '', 0, 0, '', 0, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (18, '预留菜单', '', '', '', '', 0, 0, '', 0, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (19, '预留菜单', '', '', '', '', 0, 0, '', 0, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (20, '激光打码', '', '', '', '', 0, 0, '00020', 1, '2022-11-27 11:15:55');
INSERT INTO `t_uc_role_menus` VALUES (21, '新增资讯', '', '', '', '', 1, 1, '01021', 1, '2022-11-27 13:00:55');
INSERT INTO `t_uc_role_menus` VALUES (22, '置顶/取消资讯', '', '', '', '', 1, 1, '01022', 1, '2022-11-27 13:01:20');
INSERT INTO `t_uc_role_menus` VALUES (23, '编辑资讯', '', '', '', '', 1, 1, '01023', 1, '2022-11-27 13:01:35');
INSERT INTO `t_uc_role_menus` VALUES (24, '提交资讯', '', '', '', '', 1, 1, '01024', 1, '2022-11-27 13:04:38');
INSERT INTO `t_uc_role_menus` VALUES (25, '新增游戏配置', '', '', '', '', 2, 1, '02025', 1, '2022-11-27 13:05:40');
INSERT INTO `t_uc_role_menus` VALUES (26, '编辑游戏', '', '', '', '', 2, 1, '02026', 1, '2022-11-27 13:05:47');
INSERT INTO `t_uc_role_menus` VALUES (27, '提交游戏', '', '', '', '', 2, 1, '02027', 1, '2022-11-27 13:06:03');
INSERT INTO `t_uc_role_menus` VALUES (28, '滑块详情', '', '', '', '', 2, 1, '02028', 1, '2022-11-27 13:06:14');
INSERT INTO `t_uc_role_menus` VALUES (29, '新增滑块', '', '', '', '', 2, 1, '02029', 1, '2022-11-27 13:06:23');
INSERT INTO `t_uc_role_menus` VALUES (30, '编辑滑块', '', '', '', '', 2, 1, '02030', 1, '2022-11-28 05:40:42');
INSERT INTO `t_uc_role_menus` VALUES (31, '提交滑块', '', '', '', '', 2, 1, '02031', 1, '2022-11-27 13:06:27');
INSERT INTO `t_uc_role_menus` VALUES (32, '奖品详情', '', '', '', '', 2, 1, '02032', 1, '2022-11-27 13:06:56');
INSERT INTO `t_uc_role_menus` VALUES (33, '新增奖品', '', '', '', '', 2, 1, '02033', 1, '2022-11-27 13:07:00');
INSERT INTO `t_uc_role_menus` VALUES (34, '编辑奖品', '', '', '', '', 2, 1, '02034', 1, '2022-11-27 13:07:11');
INSERT INTO `t_uc_role_menus` VALUES (35, '提交奖品', '', '', '', '', 2, 1, '02035', 1, '2022-11-27 13:07:14');
INSERT INTO `t_uc_role_menus` VALUES (36, '发送通知', '', '', '', '', 2, 1, '02036', 1, '2022-11-27 13:07:34');
INSERT INTO `t_uc_role_menus` VALUES (37, '结束此轮', '', '', '', '', 2, 1, '02037', 1, '2022-11-27 13:07:38');
INSERT INTO `t_uc_role_menus` VALUES (38, '新增活动', '', '', '', '', 3, 1, '03038', 1, '2022-11-27 13:34:02');
INSERT INTO `t_uc_role_menus` VALUES (39, '编辑活动', '', '', '', '', 3, 1, '03039', 1, '2022-11-27 13:34:05');
INSERT INTO `t_uc_role_menus` VALUES (40, '提交活动', '', '', '', '', 3, 1, '03040', 1, '2022-11-27 13:34:10');
INSERT INTO `t_uc_role_menus` VALUES (41, '奖品详情', '', '', '', '', 3, 1, '03041', 1, '2022-11-27 13:34:21');
INSERT INTO `t_uc_role_menus` VALUES (42, '新增奖品', '', '', '', '', 3, 1, '03042', 1, '2022-11-27 13:34:28');
INSERT INTO `t_uc_role_menus` VALUES (43, '编辑奖品', '', '', '', '', 3, 1, '03043', 1, '2022-11-27 13:34:31');
INSERT INTO `t_uc_role_menus` VALUES (44, '提交奖品', '', '', '', '', 3, 1, '03044', 1, '2022-11-27 13:34:34');
INSERT INTO `t_uc_role_menus` VALUES (45, '添加打折卡', '', '', '', '', 4, 1, '04045', 1, '2022-11-27 13:42:20');
INSERT INTO `t_uc_role_menus` VALUES (46, '提交打折卡', '', '', '', '', 4, 1, '04046', 1, '2022-11-27 13:42:28');
INSERT INTO `t_uc_role_menus` VALUES (47, '账户充值', '', '', '', '', 4, 1, '04047', 1, '2022-11-27 13:42:36');
INSERT INTO `t_uc_role_menus` VALUES (48, '提交充值', '', '', '', '', 4, 1, '04048', 1, '2022-11-27 13:42:39');
INSERT INTO `t_uc_role_menus` VALUES (49, '关闭/开启购买', '', '', '', '', 4, 1, '04049', 1, '2022-11-27 13:42:52');
INSERT INTO `t_uc_role_menus` VALUES (50, '抽奖记录', '', '', '', '', 5, 1, '05050', 1, '2022-11-27 13:44:35');
INSERT INTO `t_uc_role_menus` VALUES (51, '新增商品', '', '', '', '', 6, 1, '06051', 1, '2022-11-27 13:45:04');
INSERT INTO `t_uc_role_menus` VALUES (52, '编辑商品', '', '', '', '', 6, 1, '06052', 1, '2022-11-27 13:45:15');
INSERT INTO `t_uc_role_menus` VALUES (53, '提交商品', '', '', '', '', 6, 1, '06053', 1, '2022-11-27 13:45:22');
INSERT INTO `t_uc_role_menus` VALUES (54, '详情图管理', '', '', '', '', 6, 1, '06054', 1, '2022-11-27 13:45:42');
INSERT INTO `t_uc_role_menus` VALUES (55, 'SKU编辑', '', '', '', '', 6, 1, '06055', 1, '2022-11-27 13:45:47');
INSERT INTO `t_uc_role_menus` VALUES (56, 'SKU提交', '', '', '', '', 6, 1, '06056', 1, '2022-11-27 13:45:57');
INSERT INTO `t_uc_role_menus` VALUES (57, 'SKU详情图', '', '', '', '', 6, 1, '06057', 1, '2022-11-27 13:46:06');
INSERT INTO `t_uc_role_menus` VALUES (58, '促销活动', '', '', '', '', 6, 1, '06058', 1, '2022-11-27 13:46:39');
INSERT INTO `t_uc_role_menus` VALUES (59, '新增促销规则', '', '', '', '', 6, 1, '06059', 1, '2022-11-27 13:46:46');
INSERT INTO `t_uc_role_menus` VALUES (60, '编辑促销规则', '', '', '', '', 6, 1, '06060', 1, '2022-11-27 13:46:53');
INSERT INTO `t_uc_role_menus` VALUES (61, '提交促销规则', '', '', '', '', 6, 1, '06061', 1, '2022-11-27 13:46:57');
INSERT INTO `t_uc_role_menus` VALUES (62, '删除促销规则', '', '', '', '', 6, 1, '06062', 1, '2022-11-27 13:47:02');
INSERT INTO `t_uc_role_menus` VALUES (63, '首页爆款', '', '', '', '', 6, 1, '06063', 1, '2022-11-27 13:47:36');
INSERT INTO `t_uc_role_menus` VALUES (64, '新增NFT商品', '', '', '', '', 7, 1, '07064', 1, '2022-11-27 13:48:13');
INSERT INTO `t_uc_role_menus` VALUES (65, '编辑NFT', '', '', '', '', 7, 1, '07065', 1, '2022-11-27 13:48:18');
INSERT INTO `t_uc_role_menus` VALUES (66, '提交NFT', '', '', '', '', 7, 1, '07066', 1, '2022-11-27 13:48:20');
INSERT INTO `t_uc_role_menus` VALUES (67, '详情', '', '', '', '', 7, 1, '07067', 1, '2022-11-27 13:48:26');
INSERT INTO `t_uc_role_menus` VALUES (68, '新增NFT详情', '', '', '', '', 7, 1, '07068', 1, '2022-11-27 13:48:41');
INSERT INTO `t_uc_role_menus` VALUES (69, '编辑NFT详情', '', '', '', '', 7, 1, '07069', 1, '2022-11-27 13:48:49');
INSERT INTO `t_uc_role_menus` VALUES (70, '提交NFT详情', '', '', '', '', 7, 1, '07070', 1, '2022-11-27 13:49:00');
INSERT INTO `t_uc_role_menus` VALUES (71, '置顶/取消', '', '', '', '', 8, 1, '08071', 1, '2022-11-27 13:49:43');
INSERT INTO `t_uc_role_menus` VALUES (72, '下架/发布', '', '', '', '', 8, 1, '08072', 1, '2022-11-27 13:49:52');
INSERT INTO `t_uc_role_menus` VALUES (73, '删除买家秀', '', '', '', '', 8, 1, '08073', 1, '2022-11-27 13:50:04');
INSERT INTO `t_uc_role_menus` VALUES (74, '新增订单', '', '', '', '', 9, 1, '09074', 1, '2022-11-27 13:50:56');
INSERT INTO `t_uc_role_menus` VALUES (75, '开启退款', '', '', '', '', 9, 1, '09075', 1, '2022-11-27 13:51:01');
INSERT INTO `t_uc_role_menus` VALUES (76, '创建物流', '', '', '', '', 9, 1, '09076', 1, '2022-11-27 13:51:05');
INSERT INTO `t_uc_role_menus` VALUES (77, '导出订单', '', '', '', '', 9, 1, '09077', 1, '2022-11-27 13:51:09');
INSERT INTO `t_uc_role_menus` VALUES (78, '提交订单', '', '', '', '', 9, 1, '09078', 1, '2022-11-27 13:51:17');
INSERT INTO `t_uc_role_menus` VALUES (79, '退款', '', '', '', '', 9, 1, '09079', 1, '2022-11-27 13:51:22');
INSERT INTO `t_uc_role_menus` VALUES (80, '订单返佣', '', '', '', '', 9, 1, '09080', 1, '2022-11-27 13:51:25');
INSERT INTO `t_uc_role_menus` VALUES (81, '提交订单返佣', '', '', '', '', 9, 1, '09081', 1, '2022-11-27 13:51:33');
INSERT INTO `t_uc_role_menus` VALUES (82, '修改备注', '', '', '', '', 9, 1, '09082', 1, '2022-11-27 13:51:42');
INSERT INTO `t_uc_role_menus` VALUES (83, '修改顺丰运单号', '', '', '', '', 9, 1, '09083', 1, '2022-11-27 13:52:50');
INSERT INTO `t_uc_role_menus` VALUES (84, '修改艺术品编码', '', '', '', '', 9, 1, '09084', 1, '2022-11-27 13:52:57');
INSERT INTO `t_uc_role_menus` VALUES (85, '提交备注', '', '', '', '', 9, 1, '09085', 1, '2022-11-28 07:36:23');
INSERT INTO `t_uc_role_menus` VALUES (86, '提交编号', '', '', '', '', 9, 1, '09086', 1, '2022-11-28 07:36:41');
INSERT INTO `t_uc_role_menus` VALUES (87, '更新订单信息', '', '', '', '', 9, 1, '09087', 1, '2022-11-28 07:44:07');
INSERT INTO `t_uc_role_menus` VALUES (88, '补发物流', '', '', '', '', 9, 1, '09088', 1, '2022-11-30 16:52:26');
INSERT INTO `t_uc_role_menus` VALUES (89, '查询所有编码规则', '', '', '', '', 20, 1, '20089', 1, '2022-12-05 02:20:50');
INSERT INTO `t_uc_role_menus` VALUES (90, '生成随机二维码', '', '', '', '', 20, 1, '20090', 1, '2022-12-05 02:21:46');
INSERT INTO `t_uc_role_menus` VALUES (91, '生成单盒二维码', '', '', '', '', 20, 1, '20091', 1, '2022-12-05 02:22:15');
INSERT INTO `t_uc_role_menus` VALUES (92, '生成端盒二维码', '', '', '', '', 20, 1, '20092', 1, '2022-12-05 02:22:43');
INSERT INTO `t_uc_role_menus` VALUES (93, '解锁未打码的编号', '', '', '', '', 20, 1, '20093', 1, '2022-12-09 05:56:13');
INSERT INTO `t_uc_role_menus` VALUES (94, '获取订单二维码', '', '', '', '', 20, 1, '20094', 1, '2022-12-14 07:34:24');
INSERT INTO `t_uc_role_menus` VALUES (95, '更新用户分佣比例', '', '', '', '', 4, 1, '04095', 1, '2022-12-20 09:14:10');
INSERT INTO `t_uc_role_menus` VALUES (96, '更新分佣', '', '', '', '', 4, 1, '04096', 1, '2022-12-20 09:33:42');

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
INSERT INTO `t_uc_user_info` VALUES (1000000, 'Johnson测试', '中国', '上海', '上海', '宝山', 1, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLrOiaEqgufwDRaMJVf9p1YgrWxYnXwNjTN7NGvEIxCibqGvSS9r0CdPnX4ZKUfUBCialibJNhloB0dQg/132', 'IB2GQI4OPr2Oe2ZfPzuJ3gd1hFip6qAaLlLD3QZ/mttchEY6GWUX9g==', '6035e727bb1d1c1a561c40593db33873', 'https://image.1pintang9jie.com/faceimage/贾言坤_6035e727bb1d1c1a561c40593db33873.png;https://image.1pintang9jie.com/faceimage/贾言坤_6a7d9c542a29271f4aedcb3da61491f8.png;https://image.1pintang9jie.com/faceimage/贾言坤_9264873c51568c96d72be0ef1c832fbc.png;https://image.1pintang9jie.com/faceimage/贾言坤_873735d439af371ac84efd19306a9e21.png;https://image.1pintang9jie.com/faceimage/贾言坤_88c2bd15c7b4417a493fc864220d3671.png;https://image.1pintang9jie.com/faceimage/贾言坤_177d15af2598689ee4e70c8ebbbe3d9c.png;https://image.1pintang9jie.com/faceimage/贾言坤_f3c7cbf044679f7f03e8b9fe483f113f.png;https://image.1pintang9jie.com/faceimage/贾言坤_c1ba76a697b5779f40768be210350e5f.png', 1595236695272);

SET FOREIGN_KEY_CHECKS = 1;
