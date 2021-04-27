/*
Navicat MySQL Data Transfer

Source Server         : geeguard
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : vueblog

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-04-27 19:39:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `categories`
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`parent_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`child_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`category_desc`  tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=21

;

-- ----------------------------
-- Records of categories
-- ----------------------------
BEGIN;
INSERT INTO `categories` VALUES ('1', '客户端', 'Android', '聚焦Android源码解析以及项目经验复盘。包含Android UVC从定制到上层应用；图片加载库Fresco以及OkHttp源码解析，framework层源码解析。', null), ('2', '客户端', 'iOS', '', null), ('3', '客户端', '小程序', '', null), ('4', '前端', '基础', '目前业余时间主要精力在前端和后端的学习，前期通过MDN Web Docs网站学习html，css，js，所以当前有关前端的知识全部来自MDN网站，后续将与Spring Boot混合开发。', null), ('5', '前端', 'JavaScript', '', null), ('6', '前端', 'Vue', '', null), ('7', '服务端', 'Spring Boot', '随着移动App带来的巨大流量，后端的支撑越来越重要，因为移动端和前端的交互最终离不开后端的支持，目前最实用的后端技术是Spring Boot，后续将会逐步增加入门和实战文章。', null), ('8', '混合开发', 'flutter', '涉及flutter基础知识，包括各种Widget使用实战，自定义flutter Widget，翻译官方技术文档，flutter底层源码解析等。', null), ('9', '基础', '数据结构与算法', '目前仅包含了排序和二叉树相关知识，后续会追加LeetCode题目分析。', null);
COMMIT;

-- ----------------------------
-- Table structure for `music_list`
-- ----------------------------
DROP TABLE IF EXISTS `music_list`;
CREATE TABLE `music_list` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`content`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`title`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=29

;

-- ----------------------------
-- Records of music_list
-- ----------------------------
BEGIN;
INSERT INTO `music_list` VALUES ('24', '<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=530 height=86 src=\"//music.163.com/outchain/player?type=2&id=472137770&auto=0&height=66\"></iframe>', 'Heart Beat', '2021-04-27 19:35:05'), ('25', '<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=530 height=86 src=\"//music.163.com/outchain/player?type=2&id=514765154&auto=0&height=66\"></iframe>', '世界上的另一个我 - 阿肆/郭采洁', '2021-04-27 19:35:50'), ('26', '<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=530 height=86 src=\"//music.163.com/outchain/player?type=2&id=5162474&auto=0&height=66\"></iframe>', 'Yellow', '2021-04-27 19:36:24'), ('27', '<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=530 height=86 src=\"//music.163.com/outchain/player?type=2&id=2117049&auto=0&height=66\"></iframe>', 'I\'m Not Twenty', '2021-04-27 19:37:26'), ('28', '<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=530 height=86 src=\"//music.163.com/outchain/player?type=2&id=4051656&auto=0&height=66\"></iframe>', 'September', '2021-04-27 19:37:57');
COMMIT;

-- ----------------------------
-- Table structure for `roles`
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=6

;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES ('1', 'ADMIN', null), ('2', 'NORMAL', null), ('3', 'TEST', null);
COMMIT;

-- ----------------------------
-- Table structure for `roles_users`
-- ----------------------------
DROP TABLE IF EXISTS `roles_users`;
CREATE TABLE `roles_users` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`rid`  bigint NULL DEFAULT 2 ,
`uid`  bigint NULL DEFAULT NULL ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`rid`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=151

;

-- ----------------------------
-- Records of roles_users
-- ----------------------------
BEGIN;
INSERT INTO `roles_users` VALUES ('147', '1', '37', null), ('149', '2', '39', null);
COMMIT;

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
`id`  bigint NOT NULL AUTO_INCREMENT ,
`username`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`nickname`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`enable`  tinyint(1) NULL DEFAULT 1 ,
`create_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=41

;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('37', 'chuck', 'chuck', '{bcrypt}$2a$10$XcwA/Yk26yAEQyEJzyH4B.fB.yuy6enn0FvmXkIyAGU8idYdUmzam', '1', '2021-04-07 17:38:33'), ('39', 'unknownUser', 'unknownUser', '{bcrypt}$2a$10$Nsy4OEMjkBmtplCp0cB2.O5OLvQ8pQoJMKQwqUa0AuVodi3hw7rv.', '1', '2021-04-15 20:05:22');
COMMIT;

-- ----------------------------
-- Auto increment value for `categories`
-- ----------------------------
ALTER TABLE `categories` AUTO_INCREMENT=21;

-- ----------------------------
-- Auto increment value for `music_list`
-- ----------------------------
ALTER TABLE `music_list` AUTO_INCREMENT=29;

-- ----------------------------
-- Auto increment value for `roles`
-- ----------------------------
ALTER TABLE `roles` AUTO_INCREMENT=6;

-- ----------------------------
-- Indexes structure for table roles_users
-- ----------------------------
CREATE INDEX `rid` ON `roles_users`(`rid`) USING BTREE ;
CREATE INDEX `roles_users_ibfk_2` ON `roles_users`(`uid`) USING BTREE ;

-- ----------------------------
-- Auto increment value for `roles_users`
-- ----------------------------
ALTER TABLE `roles_users` AUTO_INCREMENT=151;

-- ----------------------------
-- Auto increment value for `users`
-- ----------------------------
ALTER TABLE `users` AUTO_INCREMENT=41;
