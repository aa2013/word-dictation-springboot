/*
 Navicat Premium Data Transfer

 Source Server         : word
 Source Server Type    : MySQL
 Source Server Version : 50734 (5.7.34-log)
 Source Host           : 101.43.188.139:3336
 Source Schema         : word

 Target Server Type    : MySQL
 Target Server Version : 50734 (5.7.34-log)
 File Encoding         : 65001

 Date: 30/07/2023 09:18:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lib
-- ----------------------------
DROP TABLE IF EXISTS `lib`;
CREATE TABLE `lib`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lib_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单词本名称',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'http://img.coclyun.top/wordDictation/default_cover.jpg' COMMENT '封面',
  `creator` int(11) NOT NULL COMMENT '创建者',
  `common` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否公开',
  `create_time` datetime NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_creator`(`lib_name`, `creator`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lib
-- ----------------------------
INSERT INTO `lib` VALUES (1, '总词库', '本词库中包含系统中所有的单词', 'http://img.coclyun.top/wordDictation/default_cover.jpg', 1, 1, '2023-06-13 11:34:56', '2023-06-14 21:08:51');

-- ----------------------------
-- Table structure for lib_word
-- ----------------------------
DROP TABLE IF EXISTS `lib_word`;
CREATE TABLE `lib_word`  (
  `lib_id` int(11) NOT NULL,
  `word_id` int(11) NOT NULL,
  PRIMARY KEY (`word_id`, `lib_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lib_word
-- ----------------------------

-- ----------------------------
-- Table structure for plan
-- ----------------------------
DROP TABLE IF EXISTS `plan`;
CREATE TABLE `plan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `lib_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `disorder` tinyint(4) NOT NULL DEFAULT 1,
  `ch2en` tinyint(4) NOT NULL DEFAULT 0,
  `repeat` int(11) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan
-- ----------------------------

-- ----------------------------
-- Table structure for plan_word
-- ----------------------------
DROP TABLE IF EXISTS `plan_word`;
CREATE TABLE `plan_word`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) NOT NULL,
  `word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan_word
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '$2a$10$3bggTrS.zeHmK92OwJzq8OIuNCeNvEcaKs8OSnQdHE7hCdmiRBIEe');

-- ----------------------------
-- Table structure for user_lib
-- ----------------------------
DROP TABLE IF EXISTS `user_lib`;
CREATE TABLE `user_lib`  (
  `user_id` int(11) NOT NULL,
  `lib_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`, `lib_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_lib
-- ----------------------------
INSERT INTO `user_lib` VALUES (1, 1);

-- ----------------------------
-- Table structure for word
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `word` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `us_symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `us_symbol_mp3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `en_symbol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `en_symbol_mp3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `word`(`word`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of word
-- ----------------------------

-- ----------------------------
-- Table structure for word_explain
-- ----------------------------
DROP TABLE IF EXISTS `word_explain`;
CREATE TABLE `word_explain`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `word_id` int(11) NOT NULL COMMENT '单词id',
  `lib_id` int(11) NOT NULL COMMENT '来源词本',
  `explanation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单词释义',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '词性',
  `is_default` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否为默认释义',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of word_explain
-- ----------------------------

-- ----------------------------
-- Table structure for word_explain_custom
-- ----------------------------
DROP TABLE IF EXISTS `word_explain_custom`;
CREATE TABLE `word_explain_custom`  (
  `user_id` int(10) UNSIGNED NOT NULL COMMENT '用户id',
  `lib_id` int(10) UNSIGNED NOT NULL COMMENT '单词库id',
  `word_id` int(10) UNSIGNED NOT NULL COMMENT '单词id',
  `exp_id` int(10) UNSIGNED NOT NULL COMMENT '释义id',
  PRIMARY KEY (`word_id`, `lib_id`, `exp_id`, `user_id`) USING BTREE,
  UNIQUE INDEX `one_exp`(`lib_id`, `word_id`, `exp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of word_explain_custom
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
