/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.147.128_3306
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.147.128:3306
 Source Schema         : library

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 18/12/2022 19:45:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '书籍id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '书籍isbn号',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍标题',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出版社',
  `version_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片url',
  `preface` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '书序',
  `catalogue` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '目录',
  `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容简介',
  `load` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '导读',
  `type` int NULL DEFAULT NULL COMMENT '类别',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `total` int NULL DEFAULT NULL COMMENT '总数',
  `available_num` int NULL DEFAULT NULL COMMENT '剩余数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (74, '34987-2313', '哈利波特一', '李四', '人民出版社', '6', '/file/752f6a9f2af84478adb314ddf80b1004.jpg', '书序', '目录', '简介', '导言', 1, 90.60, 6, 5);
INSERT INTO `book` VALUES (75, '34301-12310', '哈利波特二', '李四', '人民出版社', '6', '/file/1327220e22e147828c5a3ec0f29fdf12.jpg', '书序', '目录', '简介', '导言', 1, 90.60, 6, 6);
INSERT INTO `book` VALUES (76, '039247-1238', '哈利波特三', '张三', '人民出版社', '6', '/file/752f6a9f2af84478adb314ddf80b1004.jpg', '书序', '目录', '简介', '导言', 1, 182.00, 6, 6);
INSERT INTO `book` VALUES (77, '039247-961320', '哈利波特四', '张三', '人民出版社', '6', '/file/1186adb7193f4aacbb4f027ffd375f81.jpg', '书序', '目录', '简介', '导言', 1, 23.89, 6, 6);
INSERT INTO `book` VALUES (78, '274074-1238', '哈利波特五', '张三', '人民出版社', '6', '/file/752f6a9f2af84478adb314ddf80b1004.jpg', '书序', '目录', '简介', '导言', 1, 34.98, 6, 6);
INSERT INTO `book` VALUES (79, '274074-983123', '哈利波特六', '王五', '人民出版社', '6', '/file/752f6a9f2af84478adb314ddf80b1004.jpg', '书序', '目录', '简介', '导言', 1, 45.12, 6, 6);
INSERT INTO `book` VALUES (80, '034985-23', '哈利波特七', '王五', '人民出版社', '6', '/file/752f6a9f2af84478adb314ddf80b1004.jpg', '书序', '目录', '简介', '导言', 1, 54.10, 6, 6);

-- ----------------------------
-- Table structure for book_commend
-- ----------------------------
DROP TABLE IF EXISTS `book_commend`;
CREATE TABLE `book_commend`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '个推cid',
  `days` int NULL DEFAULT NULL COMMENT '推荐频率——1、3、5天',
  `version` bigint NULL DEFAULT 0 COMMENT '版本控制',
  `status` int NULL DEFAULT NULL COMMENT '1为开启推荐，0为关闭推荐',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_commend
-- ----------------------------
INSERT INTO `book_commend` VALUES (9, '057892eafbb95da07617d7feaabc5716', 5, 2, 0);
INSERT INTO `book_commend` VALUES (11, '8861bc023c0563f9c9fd87a924c40434', 3, 1, 1);

-- ----------------------------
-- Table structure for book_list
-- ----------------------------
DROP TABLE IF EXISTS `book_list`;
CREATE TABLE `book_list`  (
  `user_id` bigint NOT NULL COMMENT '用户id',
  `book_id` bigint NOT NULL COMMENT '书籍id',
  `book_isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍isbn',
  `book_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '加入书单时间',
  `borrow_time` datetime(0) NULL DEFAULT NULL COMMENT '借书时间',
  `status` int NULL DEFAULT NULL COMMENT '借书状态（0为书籍在借书栏中，1为借出未还，2为借出已还）',
  `id` int NOT NULL AUTO_INCREMENT,
  `return_time` datetime(0) NULL DEFAULT NULL COMMENT '还书时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_list
-- ----------------------------
INSERT INTO `book_list` VALUES (9, 1, '34987-2313', '哈利波特一', '2022-11-13 00:06:53', '2022-11-13 00:07:20', 2, 10, '2022-11-19 16:57:18');
INSERT INTO `book_list` VALUES (9, 2, '34987-2313', '哈利波特一', '2022-11-13 00:21:39', '2022-11-13 00:39:31', 2, 11, '2022-11-16 17:46:03');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 16:48:39', '2022-11-19 16:50:31', 2, 12, '2022-11-19 16:52:10');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 16:54:58', '2022-11-19 16:56:30', 2, 13, '2022-11-19 16:56:50');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 17:28:48', '2022-11-19 17:29:37', 2, 14, '2022-11-19 17:34:47');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 17:39:23', '2022-11-19 17:40:00', 2, 15, '2022-11-19 17:40:27');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 17:42:08', '2022-11-19 17:42:34', 2, 16, '2022-11-19 17:42:45');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 17:46:37', '2022-11-19 17:47:01', 2, 17, '2022-11-19 17:49:00');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:11:04', '2022-11-19 18:11:50', 2, 18, '2022-11-19 18:15:35');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:18:36', '2022-11-19 18:19:06', 2, 19, '2022-11-19 18:19:35');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:24:02', '2022-11-19 18:24:50', 2, 20, '2022-11-19 18:25:03');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:27:47', '2022-11-19 18:28:13', 2, 21, '2022-11-19 18:28:28');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:30:48', '2022-11-19 18:31:18', 2, 22, '2022-11-19 18:31:22');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:34:05', '2022-11-19 18:34:31', 2, 23, '2022-11-19 18:34:35');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:38:28', '2022-11-19 18:38:51', 2, 24, '2022-11-19 18:38:55');
INSERT INTO `book_list` VALUES (9, 3, '34987-2313', '哈利波特一', '2022-11-19 18:40:30', '2022-11-19 18:40:47', 2, 25, '2022-11-19 18:40:50');

-- ----------------------------
-- Table structure for book_order
-- ----------------------------
DROP TABLE IF EXISTS `book_order`;
CREATE TABLE `book_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `book_id` bigint NULL DEFAULT NULL COMMENT '书籍id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '预订时间',
  `order_time` datetime(0) NULL DEFAULT NULL COMMENT '预订取书时间',
  `status` int NULL DEFAULT 0 COMMENT '预订状态（0为预订有效；1为已取书，-1为预订失效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1601393484135157761 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_order
-- ----------------------------
INSERT INTO `book_order` VALUES (1593887995797233665, 3, 9, '2022-11-19 16:44:46', '2022-11-19 16:45:00', -1);
INSERT INTO `book_order` VALUES (1593897370951639042, 3, 9, '2022-11-19 17:22:01', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593899020378505217, 3, 9, '2022-11-19 17:28:34', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593901683564380162, 3, 9, '2022-11-19 17:39:09', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593902414790967297, 3, 9, '2022-11-19 17:42:04', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593903537216053250, 3, 9, '2022-11-19 17:46:31', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593904596424617985, 3, 9, '2022-11-19 17:50:44', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593905598523596801, 3, 9, '2022-11-19 17:54:43', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593911579336896514, 3, 9, '2022-11-19 18:18:29', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593912950316462082, 3, 9, '2022-11-19 18:23:55', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593913894391259138, 3, 9, '2022-11-19 18:27:41', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593914653103104002, 3, 9, '2022-11-19 18:30:41', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593915492224012289, 3, 9, '2022-11-19 18:34:02', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593916593463652353, 3, 9, '2022-11-19 18:38:24', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593917089956003842, 3, 9, '2022-11-19 18:40:22', '2022-11-19 19:17:21', -1);
INSERT INTO `book_order` VALUES (1593967233472765954, 3, 9, '2022-11-19 21:59:38', '2022-11-20 19:20:00', -1);
INSERT INTO `book_order` VALUES (1600663428827987970, 1, 12, '2022-12-08 09:27:55', '2022-12-08 12:00:00', -1);
INSERT INTO `book_order` VALUES (1600814594660511745, 1, 12, '2022-12-08 19:28:36', '2022-12-08 12:00:00', 0);
INSERT INTO `book_order` VALUES (1600816931642814465, 2, 12, '2022-12-08 19:37:53', '2022-12-08 12:00:00', 0);
INSERT INTO `book_order` VALUES (1601393484135157761, 38, 12, '2022-12-10 09:48:54', '2022-12-10 12:00:00', -1);

-- ----------------------------
-- Table structure for book_single
-- ----------------------------
DROP TABLE IF EXISTS `book_single`;
CREATE TABLE `book_single`  (
  `book_id` bigint NOT NULL AUTO_INCREMENT COMMENT '一本书唯一的id',
  `group_id` bigint NULL DEFAULT NULL COMMENT 'book表中的id',
  `status` int NULL DEFAULT 0 COMMENT '书状态（0为在库，1为不可获取，2为已被人预订）',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_single
-- ----------------------------
INSERT INTO `book_single` VALUES (1, 74, 2);
INSERT INTO `book_single` VALUES (2, 74, 2);
INSERT INTO `book_single` VALUES (3, 74, 0);
INSERT INTO `book_single` VALUES (4, 74, 0);
INSERT INTO `book_single` VALUES (5, 74, 0);
INSERT INTO `book_single` VALUES (6, 74, 0);
INSERT INTO `book_single` VALUES (7, 75, 0);
INSERT INTO `book_single` VALUES (8, 75, 0);
INSERT INTO `book_single` VALUES (9, 75, 0);
INSERT INTO `book_single` VALUES (10, 75, 0);
INSERT INTO `book_single` VALUES (11, 75, 0);
INSERT INTO `book_single` VALUES (12, 75, 0);
INSERT INTO `book_single` VALUES (13, 76, 0);
INSERT INTO `book_single` VALUES (14, 76, 0);
INSERT INTO `book_single` VALUES (15, 76, 0);
INSERT INTO `book_single` VALUES (16, 76, 0);
INSERT INTO `book_single` VALUES (17, 76, 0);
INSERT INTO `book_single` VALUES (18, 76, 0);
INSERT INTO `book_single` VALUES (19, 77, 0);
INSERT INTO `book_single` VALUES (20, 77, 0);
INSERT INTO `book_single` VALUES (21, 77, 0);
INSERT INTO `book_single` VALUES (22, 77, 0);
INSERT INTO `book_single` VALUES (23, 77, 0);
INSERT INTO `book_single` VALUES (24, 77, 0);
INSERT INTO `book_single` VALUES (25, 78, 0);
INSERT INTO `book_single` VALUES (26, 78, 0);
INSERT INTO `book_single` VALUES (27, 78, 0);
INSERT INTO `book_single` VALUES (28, 78, 0);
INSERT INTO `book_single` VALUES (29, 78, 0);
INSERT INTO `book_single` VALUES (30, 78, 0);
INSERT INTO `book_single` VALUES (31, 79, 0);
INSERT INTO `book_single` VALUES (32, 79, 0);
INSERT INTO `book_single` VALUES (33, 79, 0);
INSERT INTO `book_single` VALUES (34, 79, 0);
INSERT INTO `book_single` VALUES (35, 79, 0);
INSERT INTO `book_single` VALUES (36, 79, 0);
INSERT INTO `book_single` VALUES (37, 80, 0);
INSERT INTO `book_single` VALUES (38, 80, 0);
INSERT INTO `book_single` VALUES (39, 80, 0);
INSERT INTO `book_single` VALUES (40, 80, 0);
INSERT INTO `book_single` VALUES (41, 80, 0);
INSERT INTO `book_single` VALUES (42, 80, 0);

-- ----------------------------
-- Table structure for book_type
-- ----------------------------
DROP TABLE IF EXISTS `book_type`;
CREATE TABLE `book_type`  (
  `type_id` int NOT NULL COMMENT '类型id',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_type
-- ----------------------------
INSERT INTO `book_type` VALUES (1, '小说');
INSERT INTO `book_type` VALUES (2, '文学');
INSERT INTO `book_type` VALUES (3, '动漫');
INSERT INTO `book_type` VALUES (4, '传记');
INSERT INTO `book_type` VALUES (5, '艺术');
INSERT INTO `book_type` VALUES (6, '童书');
INSERT INTO `book_type` VALUES (7, '教材');
INSERT INTO `book_type` VALUES (8, '历史');
INSERT INTO `book_type` VALUES (9, '政治军事');
INSERT INTO `book_type` VALUES (10, '法律');
INSERT INTO `book_type` VALUES (11, '哲学宗教');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NULL DEFAULT NULL COMMENT '所评论帖子ID',
  `root_id` bigint NULL DEFAULT -1 COMMENT '根评论ID，若该评论为根评论，root_id=-1',
  `content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `create_by` bigint NULL DEFAULT NULL COMMENT '发评论人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建评论时间',
  `to_comment_id` bigint NULL DEFAULT -1 COMMENT '所评论的评论ID,若该评论为根评论，to_comment_id=-1',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_cryby`(`create_by`) USING BTREE,
  INDEX `idx_tocom`(`to_comment_id`) USING BTREE,
  INDEX `idx_rid_pid`(`root_id`, `post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1600671609264283649 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1600671112407031810, 1599801068135411713, -1, '评论内容', 9, '2022-12-08 09:58:27', -1);
INSERT INTO `comment` VALUES (1600671126457950210, 1599801068135411713, -1, '评论内容', 9, '2022-12-08 09:58:30', -1);
INSERT INTO `comment` VALUES (1600671477982568450, 1599801068135411713, 1600671112407031810, '评论根评论', 9, '2022-12-08 09:59:54', 1600671112407031810);
INSERT INTO `comment` VALUES (1600671609264283649, 1599801068135411713, 1600671112407031810, '评论根评论下的子评论', 9, '2022-12-08 10:00:25', 1600671477982568450);

-- ----------------------------
-- Table structure for deposit_order
-- ----------------------------
DROP TABLE IF EXISTS `deposit_order`;
CREATE TABLE `deposit_order`  (
  `out_trade_no` bigint NOT NULL AUTO_INCREMENT COMMENT '商户订单号',
  `trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝交易凭证号（交易未完成时为空）',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易名称',
  `total_amount` double NULL DEFAULT NULL COMMENT '交易金额',
  `user_id` bigint NULL DEFAULT NULL COMMENT '发布订单用户id',
  `gmt_payment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款时间',
  `trade_status` int NULL DEFAULT 0 COMMENT '交易状态（0为未支付，1为已支付，-1为退款）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '生成订单时间',
  PRIMARY KEY (`out_trade_no`) USING BTREE,
  INDEX `idx_buid`(`user_id`) USING BTREE,
  FULLTEXT INDEX `fidx_sub_rem`(`subject`)
) ENGINE = InnoDB AUTO_INCREMENT = 1550844226407886922 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of deposit_order
-- ----------------------------
INSERT INTO `deposit_order` VALUES (1550844226407886919, '2022110922001486570502140546', '增加押金', 5, 9, '2022-11-09 16:51:32', 1, '2022-11-09 16:51:08');
INSERT INTO `deposit_order` VALUES (1550844226407886921, '2022110922001486570502140547', '增加押金', 5, 9, '2022-11-09 17:17:39', -1, '2022-11-09 17:17:08');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '微信登录', 'common:wechat');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帖子内容',
  `poster_id` bigint NULL DEFAULT NULL COMMENT '发帖人id',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `coll_cnt` int NULL DEFAULT 0 COMMENT '收藏数',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `book_isbn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书籍isbn',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1601969534330232834 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1599801068135411713, '分享内容', NULL, '分享标题', 0, 0, '2022-12-06 00:21:12', '34987-2313');
INSERT INTO `post` VALUES (1599801435585802242, '分享内容', NULL, '分享标题', 0, 0, '2022-12-06 00:22:40', '34987-2313');
INSERT INTO `post` VALUES (1599801584844324866, '分享内容', 9, '分享标题', 0, 0, '2022-12-06 00:23:15', '34987-2313');
INSERT INTO `post` VALUES (1599801603999711234, '分享内容', 9, '分享标题', 0, 0, '2022-12-06 00:23:20', '34987-2313');
INSERT INTO `post` VALUES (1599802056477011969, '分享内容', 9, '分享标题', 0, 0, '2022-12-06 00:25:08', '34987-2313');
INSERT INTO `post` VALUES (1599802066065190914, '分享内容', 9, '分享标题', 0, 0, '2022-12-06 00:25:10', '34987-2313');
INSERT INTO `post` VALUES (1599802953609265154, '分享内容', 9, '分享标题', 0, 0, '2022-12-06 00:28:42', '34987-2313');
INSERT INTO `post` VALUES (1600400420268793858, '12312548949', 11, '12312548949', 0, 0, '2022-12-07 16:02:49', '');
INSERT INTO `post` VALUES (1601187946973683713, '你好', 12, 'hello', 0, 0, '2022-12-09 20:12:10', '9787208165885');
INSERT INTO `post` VALUES (1601189535977365506, '你好', 12, 'hello', 0, 0, '2022-12-09 20:18:29', '039247-961320');
INSERT INTO `post` VALUES (1601190456081833985, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 20:22:08', '039247-961320');
INSERT INTO `post` VALUES (1601191808421912577, '分享', 12, '分享', 0, 0, '2022-12-09 20:27:30', '274074-1238');
INSERT INTO `post` VALUES (1601191951250546690, 'hello分享内容', 12, 'hello分享标题', 0, 0, '2022-12-09 20:28:05', '039247-961320');
INSERT INTO `post` VALUES (1601218289231577090, 'hello', 12, 'helo', 0, 0, '2022-12-09 22:12:44', '039247-961320');
INSERT INTO `post` VALUES (1601219033976389633, 'hello', 12, 'hello', 0, 0, '2022-12-09 22:15:42', '039247-961320');
INSERT INTO `post` VALUES (1601221092704370689, 'asdasdasd', 12, 'dadawd', 0, 0, '2022-12-09 22:23:52', '039247-1238');
INSERT INTO `post` VALUES (1601221371717812226, 'hello', 12, 'hello', 0, 0, '2022-12-09 22:24:59', '039247-1238');
INSERT INTO `post` VALUES (1601222462656282625, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 22:29:19', ' 039247-1238');
INSERT INTO `post` VALUES (1601223206511902721, 'Hello', 12, 'hello', 0, 0, '2022-12-09 22:32:16', '274074-1238');
INSERT INTO `post` VALUES (1601223921728815106, 'Hello', 12, 'hello', 0, 0, '2022-12-09 22:35:07', '34301-12310');
INSERT INTO `post` VALUES (1601223972261789698, 'dadd', 12, 'hello', 0, 0, '2022-12-09 22:35:19', '34301-12310');
INSERT INTO `post` VALUES (1601224029270769665, 'adwadw', 12, 'hellodawd', 0, 0, '2022-12-09 22:35:33', '34301-12310');
INSERT INTO `post` VALUES (1601224323329228802, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 22:36:43', '34301-12310');
INSERT INTO `post` VALUES (1601224430208483330, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 22:37:08', '34301-12310');
INSERT INTO `post` VALUES (1601225101267763201, 'hello', 12, 'hellodawd', 0, 0, '2022-12-09 22:39:48', '34301-12310');
INSERT INTO `post` VALUES (1601227067138101249, 'Hello', 12, 'hello', 0, 0, '2022-12-09 22:47:37', '039247-961320');
INSERT INTO `post` VALUES (1601228443079528450, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 22:53:05', ' 274074-1238');
INSERT INTO `post` VALUES (1601231175156879362, '分享内容', 12, '分享标题', 0, 0, '2022-12-09 23:03:56', ' 274074-1238');
INSERT INTO `post` VALUES (1601232209514188801, 'Hello', 12, 'hello', 0, 0, '2022-12-09 23:08:03', '039247-1238');
INSERT INTO `post` VALUES (1601232840111017985, 'cda', 12, 'hello', 0, 0, '2022-12-09 23:10:33', '039247-1238');
INSERT INTO `post` VALUES (1601238234069757953, 'hello', 12, 'hello', 0, 0, '2022-12-09 23:31:59', '039247-1238');
INSERT INTO `post` VALUES (1601238981696032769, 'hello', 12, 'hello', 0, 0, '2022-12-09 23:34:57', '039247-1238');
INSERT INTO `post` VALUES (1601241373409869825, 'hello', 12, 'Hello', 0, 0, '2022-12-09 23:44:28', '34301-12310');
INSERT INTO `post` VALUES (1601248120409210882, 'Hello', 12, 'Hello', 0, 0, '2022-12-10 00:11:16', '34301-12310');
INSERT INTO `post` VALUES (1601252479322853377, 'Hello', 12, 'hello', 0, 0, '2022-12-10 00:28:36', '039247-961320');
INSERT INTO `post` VALUES (1601390463670046721, 'Hello', 12, 'hello', 0, 0, '2022-12-10 09:36:54', '039247-961320');
INSERT INTO `post` VALUES (1601393419274440705, 'Hello', 12, 'hello', 0, 0, '2022-12-10 09:48:38', '34301-12310');
INSERT INTO `post` VALUES (1601969534330232834, '54879', 11, '54879', 0, 0, '2022-12-11 23:57:55', '34987-2313');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '普通用户');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定微信后的open_id',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定邮箱',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称（若绑定微信，则为微信昵称，否则为随机字符串）',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像（若绑定微信，则为微信昵称，否则为默认）',
  `account` double NULL DEFAULT 0 COMMENT '用户所缴纳的押金',
  `account_rest` double NULL DEFAULT 0 COMMENT '用户可支配的押金',
  `status` int NULL DEFAULT 1 COMMENT '用户状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'odcMT5wrJWFoyalJf27yvZG4SAow', NULL, '$2a$10$llRSl5yOXJ2cVzFRlG3db.UC1MjdTYUdFmoybvlrnmYYOIS5KbDsO', '大风起兮', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJ3E7sxnawV8RMgoyst5R2YylHS3kk7ohJZ9SbE5KmgXDK5JzTiazhUpAdUIkgPj4iat0WZMY0tOotA/132', 0, 0, 1);
INSERT INTO `user` VALUES (5, 'odcMT537-RdvClaPVo2saLF5O1es', NULL, '$2a$10$vof33xHoZunp5iu2coK0TOA7fYLW46vnhmOvp8IXC9OyYHwADgYa6', '风筝有风', 'https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqE7a5UxIMkjJ3YAFFuIImaqwKoLNvlNpqoibtzc5oewh5wNSxMibtJjNyTtyGqFibwaM6f8yl0CNB0w/132', 0, 0, 1);
INSERT INTO `user` VALUES (6, 'odcMT50Ck8a1qHDlRfOrvM693wJ4', NULL, '$2a$10$yehHb1Hzs2bdvCEyXmzvUOGReAWipyN52nn0.03letE3C73lfoYVu', '乘风', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKMxTJ9OQONiax3dkFRzEaulncpibYXon4uLDQjTtl8tg37eAL4TflibF8rZsP5cJZgAeGbW4GtPGrpA/132', 0, 0, 1);
INSERT INTO `user` VALUES (8, NULL, '2427522854@qq.com', '$2a$10$/UwQaPUo9XspII2de3fzt.mpNEO4f4knC2BPjUBRQoWBsIj0.ftOy', 'sb250', NULL, 0, 0, 1);
INSERT INTO `user` VALUES (9, NULL, '1370441324@qq.com', '$2a$10$OsjSbVxmznZZ/67d5/JPvegK/ZMHNQcHyflFcQDnQs1uSLG14bd9O', 'sb250', '/file/f82e3f7cbf494fdc830d4de0b798b14b.jpg', 197, 197, 1);
INSERT INTO `user` VALUES (10, NULL, 'admin', '$2a$10$OsjSbVxmznZZ/67d5/JPvegK/ZMHNQcHyflFcQDnQs1uSLG14bd9O', NULL, NULL, 0, 0, 1);
INSERT INTO `user` VALUES (11, NULL, '1078713507@qq.com', '$2a$10$lkLWWd3kPa8x1qc24lz.uerrultm5vt4bSVcL8.eLbq7xPZY6hnWK', '0685b00c-ca83-46f6-9b2a-56a1dbe66938', NULL, 0, 0, 1);
INSERT INTO `user` VALUES (12, NULL, '1287048418@qq.com', '$2a$10$7.WozAE665jx5VSGOzVip.nYCe3XIxbe986UoU7Vf.JfrkbeLQGKW', '14817181-8711-439c-9f94-810310dc4dba', NULL, 0, 0, 1);
INSERT INTO `user` VALUES (13, NULL, '572702851@qq.com', '$2a$10$fUBevkWQXjGXXyh8nCpo3etAKN8MlJ05l8xdGZVI1gl.rOu2oIneG', '1a617423-ca01-43e2-8139-3d32cf11c2a3', NULL, 0, 0, 1);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (4, 1);
INSERT INTO `user_role` VALUES (5, 1);
INSERT INTO `user_role` VALUES (6, 1);
INSERT INTO `user_role` VALUES (8, 1);
INSERT INTO `user_role` VALUES (9, 1);
INSERT INTO `user_role` VALUES (11, 1);
INSERT INTO `user_role` VALUES (12, 1);
INSERT INTO `user_role` VALUES (13, 1);

SET FOREIGN_KEY_CHECKS = 1;
