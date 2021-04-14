CREATE DATABASE `vueblog` DEFAULT CHARACTER SET utf8;

USE `vueblog`;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(32) DEFAULT NULL,
    `create_time` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles`
VALUES (1, 'ADMIN', NULL);
INSERT INTO `roles`
VALUES (2, 'NORMAL', NULL);
INSERT INTO `roles`
VALUES (3, 'TEST', NULL);

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_users`;
CREATE TABLE `roles_users`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `rid`         bigint   DEFAULT '2',
    `uid`         bigint   DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `rid` (`rid`),
    KEY `roles_users_ibfk_2` (`uid`),
    CONSTRAINT `roles_users_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `roles` (`id`),
    CONSTRAINT `roles_users_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 131
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of roles_user
-- ----------------------------
INSERT INTO `roles_users`
VALUES (1, 1, 1, NULL);
INSERT INTO `roles_users`
VALUES (2, 2, 2, NULL);
INSERT INTO `roles_users`
VALUES (3, 3, 3, NULL);


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `username`    varchar(64)  DEFAULT NULL,
    `nickname`    varchar(64)  DEFAULT NULL,
    `password`    varchar(255) DEFAULT NULL,
    `enable`      tinyint(1)   DEFAULT '1',
    `create_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `parent_name` varchar(64) DEFAULT NULL,
    `child_name`  varchar(64) DEFAULT NULL,
    `category_desc`        tinytext    DEFAULT NULL,
    `create_time` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8;

INSERT INTO `categories`
VALUES (1, '客户端', 'Android', '聚焦Android源码解析以及项目经验复盘。包含Android UVC从定制到上层应用；图片加载库Fresco以及OkHttp源码解析，framework层源码解析。',
        NULL);
INSERT INTO `categories`
VALUES (2, '客户端', 'iOS', '', NULL);
INSERT INTO `categories`
VALUES (3, '客户端', '小程序', '', NULL);
INSERT INTO `categories`
VALUES (4, '前端', '基础', '目前业余时间主要精力在前端和后端的学习，前期通过MDN Web Docs网站学习html，css，js，所以当前有关前端的知识全部来自MDN网站，后续将与Spring Boot混合开发。',
        NULL);
INSERT INTO `categories`
VALUES (5, '前端', 'JavaScript', '', NULL);
INSERT INTO `categories`
VALUES (6, '前端', 'Vue', '', NULL);
INSERT INTO `categories`
VALUES (7, '服务端', 'Spring Boot',
        '随着移动App带来的巨大流量，后端的支撑越来越重要，因为移动端和前端的交互最终离不开后端的支持，目前最实用的后端技术是Spring Boot，后续将会逐步增加入门和实战文章。', NULL);
INSERT INTO `categories`
VALUES (8, '混合开发', 'flutter', '涉及flutter基础知识，包括各种Widget使用实战，自定义flutter Widget，翻译官方技术文档，flutter底层源码解析等。', NULL);
INSERT INTO `categories`
VALUES (9, '基础', '数据结构与算法', '目前仅包含了排序和二叉树相关知识，后续会追加LeetCode题目分析。', NULL);